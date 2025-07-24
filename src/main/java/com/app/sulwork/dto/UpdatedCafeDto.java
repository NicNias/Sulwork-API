package com.app.sulwork.dto;

import com.app.sulwork.validation.FutureOnly;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Column;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.util.List;

public record UpdatedCafeDto(
        @FutureOnly(message = "A data do café deve ser posterior à data atual")
        @Column(name = "data_cafe")
        @JsonProperty("data_cafe")
        @NotNull(message = "A data do café é obrigatória")
        LocalDate dataCafe,

        @NotNull(message = "A lista de itens é obrigatória")
        List<String> itens
) {
}
