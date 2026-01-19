package com.oscar.analytics.web;

import com.oscar.analytics.model.Student;
import com.oscar.analytics.repository.StudentRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.time.Duration;

@RestController
public class StudentController {

    private static final Logger log =
            LoggerFactory.getLogger(StudentController.class);

    private final StudentRepository repository;

    public StudentController(StudentRepository repository) {
        this.repository = repository;
    }

    @GetMapping("/students")
    public Flux<Student> getAllStudents(
            @RequestHeader(value = "X-Trace-Id", required = false) String traceId) {

        log.info("[traceId={}] Processing /students request", traceId);

        return Flux.fromIterable(repository.findAll());
    }

    @GetMapping("/students/top")
    public Flux<Student> getTopStudents(
            @RequestParam double min,
            @RequestHeader(value = "X-Trace-Id", required = false) String traceId) {

        log.info("[traceId={}] Processing /students/top", traceId);

        return Flux.fromIterable(repository.findAll())
                .filter(s -> s.getAverageGrade() >= min);
    }

    @GetMapping("/students/stream")
    public Flux<Student> streamStudents() {
        return Flux.fromIterable(repository.findAll())
                .delayElements(Duration.ofSeconds(1));
    }
}
