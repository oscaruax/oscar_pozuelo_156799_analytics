package com.oscar.analytics.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "students")
public class Student {

    @Id
    private Long id;

    private String name;

    private Double averageGrade;

    public Student() {}

    public Student(Long id, String name, Double averageGrade) {
        this.id = id;
        this.name = name;
        this.averageGrade = averageGrade;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Double getAverageGrade() {
        return averageGrade;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAverageGrade(Double averageGrade) {
        this.averageGrade = averageGrade;
    }
}
