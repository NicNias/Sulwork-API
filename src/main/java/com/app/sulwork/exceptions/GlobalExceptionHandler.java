package com.app.sulwork.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.List;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(BaseException.class)
    public ResponseEntity<ProblemDetail> handleBaseException(BaseException ex) {
        ProblemDetail pb = mapToProblemDetail(ex);
        return ResponseEntity.status(pb.getStatus()).body(pb);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ProblemDetail> handleValidationExceptions(MethodArgumentNotValidException ex) {
        List<String> errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(FieldError::getDefaultMessage)
                .toList();

        ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);
        problemDetail.setTitle("Erro de validação");
        problemDetail.setDetail(String.join("; ", errors));

        return ResponseEntity.badRequest().body(problemDetail);
    }

    ProblemDetail mapToProblemDetail(BaseException ex) {
        HttpStatus httpStatus = HttpStatus.resolve(Integer.parseInt(ex.getHttpStatusCode()));
        assert httpStatus != null;
        ProblemDetail pb = ProblemDetail.forStatus(httpStatus);
        pb.setTitle(ex.getTitle());
        pb.setDetail(ex.getDetail());
        return pb;
    }
}