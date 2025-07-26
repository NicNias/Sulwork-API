package com.app.sulwork.exceptions.colaboradores;


import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ColaboradoresNotFoundExceptionUnitTest {
    @Test
    void criarExcecaoComDetalheCorreto() {
        String detalhe = "Nenhum colaborador com o ID 123 encontrado";

        ColaboradoresNotFoundException exception = new ColaboradoresNotFoundException(detalhe);

        assertThat(exception.getHttpStatusCode()).isEqualTo("404");
        assertThat(exception.getTitle()).isEqualTo("Colaboradores não encontrados na base de dados!");
        assertThat(exception.getDetail()).isEqualTo(detalhe);
    }
}
