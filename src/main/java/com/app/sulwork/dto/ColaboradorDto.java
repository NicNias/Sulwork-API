package com.app.sulwork.dto;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

import com.app.sulwork.validation.FutureOnly;

public record ColaboradorDto(
        UUID id,

        @NotBlank(message = "O campo nome é obrigatório")
        String nome,

        @NotBlank(message = "O campo cpf é obrigatório")
        @Pattern(regexp = "\\d{11}", message = "O CPF deve conter apenas números. EX: 99999999999")
        String cpf,

        LocalDate date,
        @FutureOnly
        List<String> itens,

        Boolean entregue
) {
}
