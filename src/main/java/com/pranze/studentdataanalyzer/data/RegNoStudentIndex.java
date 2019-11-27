package com.pranze.studentdataanalyzer.data;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class RegNoStudentIndex {

    private final Map<String, Student> students = new ConcurrentHashMap<>();

    public void addStudent(Student student) {
        if (students.get(student.getRegNo()) != null) {
            throw new IllegalArgumentException("Student RegNo already there");
        }
        students.put(student.getRegNo(), student);
    }

    public Student getStudentByRegNo(String regNo) {
        return students.get(regNo);
    }
}
