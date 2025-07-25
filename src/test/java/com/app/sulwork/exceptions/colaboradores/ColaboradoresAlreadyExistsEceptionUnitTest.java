package com.app.sulwork.exceptions.colaboradores;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class ColaboradoresAlreadyExistsEceptionUnitTest {
    @Test
    void criarExcecaoComDetalheCorreto() {
        String detalhe = "CPF 12345678900 já está em uso";

        ColaboradoresAlreadyExistsEception exception = new ColaboradoresAlreadyExistsEception(detalhe);

        assertThat(exception.getHttpStatusCode()).isEqualTo("400");
        assertThat(exception.getTitle()).isEqualTo("Colaborador ja cadastrado!");
        assertThat(exception.getDetail()).isEqualTo(detalhe);
    }
}
