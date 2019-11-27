package com.pranze.studentdataanalyzer.data;

import java.util.*;

public class SubjectScoreIndex {

    private Map<String, TreeMap<Integer, List<Student>>> subjectScoreMap = new HashMap<>();

    public void addScore(String subject, Student student, Integer score) {
        TreeMap<Integer, List<Student>> map = subjectScoreMap.computeIfAbsent(subject, s -> new TreeMap<>());
        List<Student> students = map.computeIfAbsent(score, s -> new LinkedList<>());
        students.add(student);
        student.setScore(subject, score);
    }

    public TreeMap<Integer, List<Student>> getScoresTree(String subject) {
        return subjectScoreMap.get(subject);
    }
}
