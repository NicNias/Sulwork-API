package com.app.sulwork.exceptions;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class BaseExceptionUnitTest {
    @Test
    void criarBaseExceptionComDadosCorretos() {
        String statusCode = "404";
        String title = "Recurso não encontrado";
        String detail = "O colaborador com ID 123 não existe";

        BaseException exception = new BaseException(statusCode, title, detail);

        assertThat(exception.getHttpStatusCode()).isEqualTo("404");
        assertThat(exception.getTitle()).isEqualTo("Recurso não encontrado");
        assertThat(exception.getDetail()).isEqualTo("O colaborador com ID 123 não existe");
    }
}
