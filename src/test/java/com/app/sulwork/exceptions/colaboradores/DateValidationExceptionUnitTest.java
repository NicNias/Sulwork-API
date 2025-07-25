package com.app.sulwork.exceptions.colaboradores;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class DateValidationExceptionUnitTest {
    @Test
    void criarExcecaoComDetalheCorreto() {
        String detalhe = "Data fornecida é inválida ou está no passado";

        DateValidationException exception = new DateValidationException(detalhe);

        assertThat(exception.getHttpStatusCode()).isEqualTo("400");
        assertThat(exception.getTitle()).isEqualTo("Time validation error!");
        assertThat(exception.getDetail()).isEqualTo(detalhe);
    }
}
