package com.pranze.studentdataanalyzer.data;

import java.util.*;

public class Course {

    private final String name;
    private final Map<String, Batch> batches = new HashMap<>();
    private final Set<String> subjects = new HashSet<>();

    public Course(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void addBatch(Batch batch) {
        batches.put(batch.getName(), batch);
        batch.setCourse(this);
    }

    public Batch getOrAddBatch(String name) {
        return batches.computeIfAbsent(name, s -> {
            Batch batch = new Batch(s);
            batch.setCourse(this);
            return batch;
        });
    }

    public Batch getBatchByName(String name) {
        return batches.get(name);
    }

    public Collection<Batch> getAllBatches() {
        return batches.values();
    }

    public void addSubject(String subject) {
        subjects.add(subject);
    }

    public Collection<String> getAllSubjects() {
        return subjects;
    }
}
