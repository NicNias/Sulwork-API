package com.app.sulwork.dto;

import java.time.LocalDate;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Column;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

import com.app.sulwork.validation.FutureOnly;

public record ColaboradorDto(
        String id,

        @NotBlank(message = "O campo nome é obrigatório")
        String nome,

        @NotBlank(message = "O campo cpf é obrigatório")
        @Pattern(regexp = "\\d{11}", message = "O CPF deve conter apenas números. EX: 99999999999")
        String cpf,

        @NotNull
        @FutureOnly(message = "A data do café deve ser posterior à data atual")
        @Column(name = "data_cafe")
        @JsonProperty("data_cafe")
        LocalDate dataCafe,

        List<String> itens,

        Boolean entregue
) {
}
