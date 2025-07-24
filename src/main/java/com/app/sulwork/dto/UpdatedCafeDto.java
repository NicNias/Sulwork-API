package com.app.sulwork.dto;

import jakarta.validation.constraints.NotNull;

import java.util.List;

public record UpdatedCafeDto(
        @NotNull(message = "A lista de itens é obrigatória")
        List<String> itens
) {
}
