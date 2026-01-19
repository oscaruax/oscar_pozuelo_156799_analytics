package com.oscar.analytics.repository;

import com.oscar.analytics.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudentRepository extends JpaRepository<Student, Long> {
}
