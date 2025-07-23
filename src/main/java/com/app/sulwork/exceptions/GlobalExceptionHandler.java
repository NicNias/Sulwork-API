package com.app.sulwork.exceptions;

import com.app.sulwork.dto.ExceptionResponseDto;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(BaseException.class)
    public ResponseEntity<ProblemDetail> handleCustomException(BaseException exception) {
        ProblemDetail probD = exception.problemDetail();
        return ResponseEntity.status(probD.getStatus()).body(probD);
    }
}
