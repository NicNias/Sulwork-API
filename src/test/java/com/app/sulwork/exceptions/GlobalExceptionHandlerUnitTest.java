package com.app.sulwork.exceptions;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class GlobalExceptionHandlerUnitTest {
    private GlobalExceptionHandler handler;

    @BeforeEach
    void setup() {
        handler = new GlobalExceptionHandler();
    }

    @Test
    void tratarMethodArgumentNotValidExceptionERetornarProblemDetailCorreto() {
        // Arrange
        MethodArgumentNotValidException ex = Mockito.mock(MethodArgumentNotValidException.class);
        FieldError fieldError = new FieldError("objeto", "campo", "Campo inválido");
        BindingResult bindingResult = Mockito.mock(BindingResult.class);

        Mockito.when(ex.getBindingResult()).thenReturn(bindingResult);
        Mockito.when(bindingResult.getFieldErrors()).thenReturn(List.of(fieldError));

        // Act
        ResponseEntity<ProblemDetail> response = handler.handleValidationExceptions(ex);

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        ProblemDetail pb = response.getBody();
        assertThat(pb).isNotNull();
        assertThat(pb.getTitle()).isEqualTo("Erro de validação");
        assertThat(pb.getDetail()).isEqualTo("Campo inválido");
    }

    @Test
    void tratarBaseExceptionERetornarProblemDetailCorreto() {
        // Arrange
        BaseException ex = new BaseException("400", "Erro de negócio", "Colaborador já existe");

        // Act
        ResponseEntity<ProblemDetail> response = handler.handleBaseException(ex);

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);

        ProblemDetail pb = response.getBody();
        assertThat(pb).isNotNull();
        assertThat(pb.getTitle()).isEqualTo("Erro de negócio");
        assertThat(pb.getDetail()).isEqualTo("Colaborador já existe");
        assertThat(pb.getStatus()).isEqualTo(400);
    }
}
