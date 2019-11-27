package com.pranze.studentdataanalyzer;

import com.pranze.studentdataanalyzer.data.*;
import com.pranze.studentdataanalyzer.dto.StudentDTO;
import com.pranze.studentdataanalyzer.query.Order;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.Map.Entry;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class StudentDataAnalyzer {

    private final CourseNameIndex courseNameIndex = new CourseNameIndex();
    private final RegNoStudentIndex regNoStudentIndex = new RegNoStudentIndex();
    private final SubjectScoreIndex subjectScoreIndex = new SubjectScoreIndex();

    public void loadData(File studentDataFile, File scoreDataFile) throws IOException {
        try (BufferedReader studDsReader = new BufferedReader(new FileReader(studentDataFile));
             BufferedReader scoreDsReader = new BufferedReader(new FileReader(scoreDataFile))) {
            loadStudentData(studDsReader);
            loadScoreData(scoreDsReader);
        }
    }

    private void loadStudentData(BufferedReader dsReader) throws IOException {
        // Finding the positions of the data by knowing the headers
        final String[] headers = Optional.ofNullable(dsReader.readLine())
                .orElseThrow(() -> new IllegalArgumentException("Invalid DataSet")).split(",");
        if (headers.length < 3) {
            throw new IllegalArgumentException("Invalid DataSet");
        }

        final int[] headIndexes = new int[5];
        for (int i = 0; i < headers.length; i++) {
            switch (headers[i]) {
                case "RegNo":
                    headIndexes[0] = i;
                    break;
                case "Name":
                    headIndexes[1] = i;
                    break;
                case "Section":
                    headIndexes[2] = i;
                    break;
                case "Batch":
                    headIndexes[3] = i;
                    break;
                case "Course":
                    headIndexes[4] = i;
                    break;
            }
        }

        dsReader.lines().map(s -> s.split(",")).forEach(splits -> {
            String regNo = splits[headIndexes[0]];
            String name = splits[headIndexes[1]];
            String section = splits[headIndexes[2]];
            String batch = splits[headIndexes[3]];
            String course = splits[headIndexes[4]];
            Student student = new Student(regNo, name);
            courseNameIndex.getOrAddCourse(course).getOrAddBatch(batch).getOrAddSection(section)
                    .addStudent(student);
            regNoStudentIndex.addStudent(student);
        });
    }

    private void loadScoreData(BufferedReader dsReader) throws IOException {
        // Finding the positions of the data by knowing the headers
        final String[] headers = Optional.ofNullable(dsReader.readLine())
                .orElseThrow(() -> new IllegalArgumentException("Invalid DataSet")).split(",");
        if (headers.length < 3) {
            throw new IllegalArgumentException("Invalid DataSet");
        }

        final int[] headIndexes = new int[3];
        for (int i = 0; i < headers.length; i++) {
            switch (headers[i]) {
                case "RegNo":
                    headIndexes[0] = i;
                    break;
                case "Subject":
                    headIndexes[1] = i;
                    break;
                case "Score":
                    headIndexes[2] = i;
                    break;
            }
        }

        dsReader.lines().map(s -> s.split(",")).forEach(splits -> {
            String regNo = splits[headIndexes[0]];
            String subject = splits[headIndexes[1]];
            int score = Integer.parseInt(splits[headIndexes[2]]);
            Student student = regNoStudentIndex.getStudentByRegNo(regNo);
            if (student != null) {
                subjectScoreIndex.addScore(subject, student, score);
            }
        });
    }

    /**
     * @param filter available keys are course, batch, section, subject
     * @param order  possible values
     *               1. totalScore
     *               2. avgScore
     *               3. score (only if subject is in filter)
     * @param limit
     * @return
     */
    public List<StudentDTO> query(Map<String, String> filter, Order order, int limit) {
        String courseName = filter != null ? filter.get("course") : null;
        String batchName = filter != null ? filter.get("batch") : null;
        String sectionName = filter != null ? filter.get("section") : null;
        Stream<Student> studentStream = filteredByGroups(courseName, batchName, sectionName);
        if (studentStream == null) {
            return Collections.emptyList();
        }

        Function<Student, StudentDTO> convertDto = s -> {
            Section section = s.getSection();
            Batch batch = section.getBatch();
            Course course = batch.getCourse();
            return new StudentDTO(s.getRegNo(), s.getName(), section.getName(),
                    batch.getName(), course.getName());
        };
        if (order == null) {
            if (limit > 0) {
                studentStream = studentStream.limit(limit);
            }
            return studentStream.map(convertDto).collect(Collectors.toList());
        }

        if (order.getOrderBy().equals("score")) {
            String subjectFilter = filter.get("subject");
            if (subjectFilter == null) {
                throw new IllegalArgumentException("when orderBy is score, subject filter is must");
            }
            Map<Integer, List<Student>> scoresMap = !order.isDesc()
                    ? subjectScoreIndex.getScoresTree(subjectFilter)
                    : subjectScoreIndex.getScoresTree(subjectFilter).descendingMap();
            final Set<Student> studSet = studentStream.collect(Collectors.toSet());
            final List<StudentDTO> dtos = new LinkedList<>();
            for (Entry<Integer, List<Student>> entry : scoresMap.entrySet()) {
                if (limit > 0 && dtos.size() == limit) {
                    break;
                }
                Stream<Student> stream = entry.getValue().stream();
                if (limit > 0) {
                    stream = stream.limit(limit - dtos.size());
                }
                stream.filter(studSet::contains).map(convertDto).forEach(dto -> {
                    dto.setSubject(subjectFilter);
                    dto.setScore(entry.getKey());
                    dtos.add(dto);
                });
            }
            return dtos;
        }
        Function<Student, StudentDTO> convertDtoWithScore = convertDto;
        if (order.getOrderBy().equals("totalScore")) {
            studentStream = studentStream.sorted((o1, o2) -> {
                if (!order.isDesc()) {
                    return Integer.compare(o1.getTotalScore(), o2.getTotalScore());
                } else {
                    return Integer.compare(o2.getTotalScore(), o1.getTotalScore());
                }
            });
            convertDtoWithScore = student -> {
                StudentDTO dto = convertDto.apply(student);
                dto.setScore(student.getTotalScore());
                return dto;
            };
        } else if (order.getOrderBy().equals("avgScore")) {
            studentStream = studentStream.sorted((o1, o2) -> {
                if (!order.isDesc()) {
                    return Double.compare(o1.getAvgScore(), o2.getAvgScore());
                } else {
                    return Double.compare(o2.getAvgScore(), o1.getAvgScore());
                }
            });
            convertDtoWithScore = student -> {
                StudentDTO dto = convertDto.apply(student);
                dto.setScore(student.getAvgScore());
                return dto;
            };
        }
        if (limit > 0) {
            studentStream = studentStream.limit(limit);
        }
        return studentStream.map(convertDtoWithScore).collect(Collectors.toList());
    }

    private Stream<Student> filteredByGroups(String courseName, String batchName, String sectionName) {
        Stream<Student> studentStream = null;
        if (courseName != null) {
            Course course = courseNameIndex.getCourseByName(courseName);
            if (course != null && batchName != null) {
                Batch batch = course.getBatchByName(batchName);
                if (batch != null && sectionName != null) {
                    Section section = batch.getSectionByName(sectionName);
                    if (section != null) {
                        studentStream = section.getAllStudents().stream();
                    }
                } else if (batch != null) {
                    studentStream = batch.getAllSections().stream()
                            .map(Section::getAllStudents).flatMap(Collection::stream);
                }
            } else if (course != null) {
                studentStream = course.getAllBatches().stream()
                        .map(Batch::getAllSections).flatMap(Collection::stream)
                        .map(Section::getAllStudents).flatMap(Collection::stream);
            }
        } else {
            studentStream = courseNameIndex.getAllCourses().stream()
                    .map(Course::getAllBatches).flatMap(Collection::stream)
                    .map(Batch::getAllSections).flatMap(Collection::stream)
                    .map(Section::getAllStudents).flatMap(Collection::stream);
        }
        return studentStream;
    }
}
