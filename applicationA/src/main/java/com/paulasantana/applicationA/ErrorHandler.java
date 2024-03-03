package com.paulasantana.applicationA;

import io.github.resilience4j.bulkhead.BulkheadFullException;
import io.github.resilience4j.ratelimiter.RequestNotPermitted;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ErrorHandler {

    private static final Logger logger = LoggerFactory.getLogger(ErrorHandler.class);

    @ExceptionHandler({ RequestNotPermitted.class })
    @ResponseStatus(HttpStatus.TOO_MANY_REQUESTS)
    public void handleRequestNotPermitted() {
        logger.error("--------------TOO_MANY_REQUESTS-----------------");
    }

    @ExceptionHandler({ BulkheadFullException.class })
    @ResponseStatus(HttpStatus.BANDWIDTH_LIMIT_EXCEEDED)
    public void handleBulkheadFullException() {
        logger.error("--------------BANDWIDTH_LIMIT_EXCEEDED-----------------");
    }

    @ExceptionHandler({ ClientApplicationBException.class })
    public ResponseEntity<ErrorResponse> handleClientApplicationB(ClientApplicationBException e) {
        logger.error("--------------INTERNAL_SERVER_ERROR-----------------");
        return ResponseEntity.internalServerError()
                .body(ErrorResponse.builder(e, HttpStatus.INTERNAL_SERVER_ERROR,e.getMessage()).build());
    }
}
