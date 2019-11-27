package com.pranze.studentdataanalyzer.data;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class CourseNameIndex {

    private final Map<String, Course> courses = new HashMap<>();

    public void addCourse(Course course) {
        courses.put(course.getName(), course);
    }

    public Course getOrAddCourse(String course) {
        return courses.computeIfAbsent(course, Course::new);
    }

    public Course getCourseByName(String name) {
        return courses.get(name);
    }

    public Collection<Course> getAllCourses() {
        return courses.values();
    }
}
