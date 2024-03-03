package com.paulasantana.applicationA;

import io.github.resilience4j.bulkhead.annotation.Bulkhead;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/domainA")
public class ControllerA {

    ClientApplicationB clientApplicationB;

    public ControllerA(ClientApplicationB clientApplicationB) {
        this.clientApplicationB = clientApplicationB;
    }

    @GetMapping("/retry")
    public ResponseEntity<?> processRetry(){

        var response = clientApplicationB.methodCallWithRetryExample();

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/timelimiter")
    public ResponseEntity<?> processTimelimiter(){

        CompletableFuture<String> result = clientApplicationB.methodCallWithTimeLimiterExample();
        String response = result.join();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/circuitbreaker")
    public ResponseEntity<?> processCircuitBreaker(){

        var response = clientApplicationB.methodCallWithCircuitBreakerExample();

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/ratelimiter")
    @RateLimiter(name = "applicationB")
    public ResponseEntity<?> processRateLimiter(){

        var response = clientApplicationB.methodCallWithCircuitBreakerExample();

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/bulkhead")
    @Bulkhead(name = "applicationB")
    public ResponseEntity<?> processBulkhead(){

        var response = clientApplicationB.methodCallWithTimeLimiterExample();

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
