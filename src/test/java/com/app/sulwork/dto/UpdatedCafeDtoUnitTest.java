package com.app.sulwork.dto;

import jakarta.validation.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class UpdatedCafeDtoUnitTest {
    private Validator validator;

    @BeforeEach
    void setup() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void validoComListaDeItens() {
        UpdatedCafeDto dto = new UpdatedCafeDto(List.of("Pão", "Leite"));

        Set<ConstraintViolation<UpdatedCafeDto>> violations = validator.validate(dto);

        assertThat(violations).isEmpty();
    }

    @Test
    void falharQuandoListaDeItensForNula() {
        UpdatedCafeDto dto = new UpdatedCafeDto(null);

        Set<ConstraintViolation<UpdatedCafeDto>> violations = validator.validate(dto);

        assertThat(violations)
                .hasSize(1)
                .anyMatch(v -> v.getPropertyPath().toString().equals("itens")
                        && v.getMessage().equals("A lista de itens é obrigatória"));
    }
}
