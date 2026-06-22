package com.example.employment.common.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.io.PrintWriter;
import java.io.StringWriter;

@RestControllerAdvice
@Profile("dev")
public class DevExceptionHandler {
    private static final Logger log = LoggerFactory.getLogger(DevExceptionHandler.class);

    @ExceptionHandler(Throwable.class)
    public ResponseEntity<String> handle(Throwable ex) {
        StringWriter sw = new StringWriter();
        ex.printStackTrace(new PrintWriter(sw));
        String stack = sw.toString();
        log.error("Unhandled exception: {}", stack);
        return ResponseEntity.status(500)
                .contentType(MediaType.TEXT_PLAIN)
                .body(stack);
    }
}