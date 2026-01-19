package com.oscar.analytics.gateway;

import com.oscar.analytics.model.Student;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.util.UUID;

@RestController
public class GatewayController {

    private static final Logger log =
            LoggerFactory.getLogger(GatewayController.class);

    private final StudentClientService clientService;

    public GatewayController(StudentClientService clientService) {
        this.clientService = clientService;
    }

    @GetMapping("/api/public/students")
    public ResponseEntity<Flux<Student>> getStudentsThroughGateway() {

        String traceId = UUID.randomUUID().toString();
        MDC.put("traceId", traceId);

        log.info("[traceId={}] Incoming external request", traceId);

        Flux<Student> body = clientService.getStudents(traceId)
                .doFinally(signal -> MDC.clear());

        HttpHeaders headers = new HttpHeaders();
        headers.add("X-Trace-Id", traceId);

        return ResponseEntity.ok()
                .headers(headers)
                .body(body);
    }
}
