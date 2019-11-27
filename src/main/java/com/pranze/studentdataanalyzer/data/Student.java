package com.pranze.studentdataanalyzer.data;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Student {

    private final String regNo;
    private final String name;
    private Section section;
    private final Map<String, Integer> scores = new HashMap<>();
    private int totalScore = 0;
    private double avgScore = 0;

    public Student(String regNo, String name) {
        this.regNo = regNo;
        this.name = name;
    }

    public void setScore(String subject, int score) {
        totalScore -= scores.getOrDefault(subject, 0);
        scores.put(subject, score);
        totalScore += score;
        avgScore = ((double) totalScore) / scores.size();
    }

    public Integer getScoreBySubject(String subject) {
        return scores.getOrDefault(subject, 0);
    }

    public int getTotalScore() {
        return totalScore;
    }

    public String getRegNo() {
        return regNo;
    }

    public String getName() {
        return name;
    }

    public Section getSection() {
        return section;
    }

    public void setSection(Section section) {
        this.section = section;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Student student = (Student) o;
        return Objects.equals(regNo, student.regNo);
    }

    @Override
    public int hashCode() {
        return Objects.hash(regNo);
    }

    public double getAvgScore() {
        return avgScore;
    }
}
