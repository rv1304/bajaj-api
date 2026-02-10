package com.example.demo.exception;

import com.example.demo.dto.BfhlResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @Value("${chitkara.email}")
    private String email;

    @ExceptionHandler(Exception.class)
    public ResponseEntity<BfhlResponse<String>> handleError(Exception e) {
        HttpStatus status = HttpStatus.BAD_REQUEST;
        if (e.getMessage().contains("server error")) status = HttpStatus.INTERNAL_SERVER_ERROR;

        return new ResponseEntity<>(BfhlResponse.<String>builder()
                .isSuccess(false)
                .officialEmail(email)
                .data("Error: " + e.getMessage())
                .build(), status);
    }
}
