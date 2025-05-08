package com.example.healthcarescheduler.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class AppointmentException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public AppointmentException(String message) {
        super(message);
    }
}