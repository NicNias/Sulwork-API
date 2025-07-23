package com.app.sulwork.exceptions;

import com.app.sulwork.dto.ExceptionResponseDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(CustomException.class)
    public ResponseEntity<ExceptionResponseDto> handleCustomException(CustomException ex) {
        ExceptionResponseDto response = new ExceptionResponseDto(
                ex.getMessage(),
                ex.getErrors()
        );
        return ResponseEntity.status(ex.getHttpStatus()).body(response);
    }

    @ExceptionHandler(BaseException.class)
    public ResponseEntity<ProblemDetail> handleUnauthorizedInSecrity(BaseException exception) {
        ProblemDetail probD = exception.problemDetail();
        return ResponseEntity.status(probD.getStatus()).body(probD);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ExceptionResponseDto> handleValidationException(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error ->
                errors.put(error.getField(), error.getDefaultMessage())
        );

        ExceptionResponseDto response = new ExceptionResponseDto(
                "Erro nos campos enviados",
                errors
        );

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ExceptionResponseDto> handleGlobalException(Exception ex) {
        ExceptionResponseDto response = new ExceptionResponseDto(
                "Ocorreu um erro inesperado. Por favor, tente novamente mais tarde.",
                null
        );
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
