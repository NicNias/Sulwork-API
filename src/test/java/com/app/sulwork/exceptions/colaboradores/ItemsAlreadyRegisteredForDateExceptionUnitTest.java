package com.app.sulwork.exceptions.colaboradores;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class ItemsAlreadyRegisteredForDateExceptionUnitTest {
    @Test
    void criarExcecaoComDetalheCorreto() {
        String detalhe = "O item 'Pão' já foi registrado para 2025-07-25";

        ItemsAlreadyRegisteredForDateException exception = new ItemsAlreadyRegisteredForDateException(detalhe);

        assertThat(exception.getHttpStatusCode()).isEqualTo("400");
        assertThat(exception.getTitle()).isEqualTo("Item ja cadastrado para está data!");
        assertThat(exception.getDetail()).isEqualTo(detalhe);
    }
}
