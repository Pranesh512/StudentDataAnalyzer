package com.pranze.studentdataanalyzer.dto;

public class StudentDTO {

    private String regNo;
    private String name;
    private String section;
    private String batch;
    private String course;
    private String subject;
    private Number score;

    public StudentDTO() {
    }

    public StudentDTO(String regNo, String name, String section, String batch, String course) {
        this.regNo = regNo;
        this.name = name;
        this.section = section;
        this.batch = batch;
        this.course = course;
    }

    public String getRegNo() {
        return regNo;
    }

    public void setRegNo(String regNo) {
        this.regNo = regNo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSection() {
        return section;
    }

    public void setSection(String section) {
        this.section = section;
    }

    public String getBatch() {
        return batch;
    }

    public void setBatch(String batch) {
        this.batch = batch;
    }

    public String getCourse() {
        return course;
    }

    public void setCourse(String course) {
        this.course = course;
    }

    public Number getScore() {
        return score;
    }

    public void setScore(Number score) {
        this.score = score;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }
}
