package com.oscar.analytics.gateway;

import com.oscar.analytics.model.Student;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

import java.time.Duration;

@Service
public class StudentClientService {

    private static final Logger log =
            LoggerFactory.getLogger(StudentClientService.class);

    private final WebClient webClient;

    public StudentClientService(WebClient webClient) {
        this.webClient = webClient;
    }

    @CircuitBreaker(name = "studentService", fallbackMethod = "fallback")
    public Flux<Student> getStudents(String traceId) {

        log.info("[traceId={}] Calling student service", traceId);

        return webClient.get()
                .uri("/students")
                .header("X-Trace-Id", traceId)
                .retrieve()
                .bodyToFlux(Student.class)
                // timeout artificial → permite que el CB se active
                .timeout(Duration.ofSeconds(3));
    }

    public Flux<Student> fallback(String traceId, Throwable ex) {
        log.warn("[traceId={}] Fallback activated: {}", traceId, ex.toString());
        return Flux.empty();
    }
}
