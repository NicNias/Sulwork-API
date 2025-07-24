package com.app.sulwork.exceptions;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;

import java.util.Map;

public class UserAlreadyExistsException extends BaseException {
    public UserAlreadyExistsException(String detail) {
        super("400", "User validation error!", detail);
    }
}
