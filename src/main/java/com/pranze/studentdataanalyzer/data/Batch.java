package com.pranze.studentdataanalyzer.data;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class Batch {

    private final String name;
    private final Map<String, Section> sections = new HashMap<>();
    private Course course;

    public Batch(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void addSection(Section section) {
        sections.put(section.getName(), section);
        section.setBatch(this);
    }

    public Section getOrAddSection(String name) {
        return sections.computeIfAbsent(name, s -> {
            Section section = new Section(s);
            section.setBatch(this);
            return section;
        });
    }

    public Section getSectionByName(String name) {
        return sections.get(name);
    }

    public Collection<Section> getAllSections() {
        return sections.values();
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }
}
