package com.app.sulwork.exceptions;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;

import java.util.Map;

public class DateValidationException extends BaseException {
    public DateValidationException(String detail) {
        super("400", "Time validation error!", detail);
    }
}
