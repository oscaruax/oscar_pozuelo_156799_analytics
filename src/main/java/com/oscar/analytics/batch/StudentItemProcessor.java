package com.oscar.analytics.batch;

import com.oscar.analytics.model.Student;
import org.springframework.batch.item.ItemProcessor;

public class StudentItemProcessor implements ItemProcessor<Student, Student> {

    @Override
    public Student process(Student student) {
        student.setName(student.getName().toUpperCase());
        student.setAverageGrade(Math.round(student.getAverageGrade() * 10.0) / 10.0);
        return student;
    }
}
