package com.app.sulwork.exceptions;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;

public class GlobalExceptionHandlerUnitTest {
    private GlobalExceptionHandler handler;

    @BeforeEach
    void setup() {
        handler = new GlobalExceptionHandler();
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
