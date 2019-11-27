package com.pranze.studentdataanalyzer.data;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

public class Section {

    private final String name;
    private final List<Student> students = new LinkedList<>();
    private Batch batch;

    public Section(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void addStudent(Student student) {
        students.add(student);
        student.setSection(this);
    }

    public Collection<Student> getAllStudents() {
        return students;
    }

    public Batch getBatch() {
        return batch;
    }

    public void setBatch(Batch batch) {
        this.batch = batch;
    }
}
