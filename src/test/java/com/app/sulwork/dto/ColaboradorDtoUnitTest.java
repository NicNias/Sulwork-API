package com.app.sulwork.dto;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class ColaboradorDtoUnitTest {
    private Validator validator;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    private ColaboradorDto buildValidDto() {
        return new ColaboradorDto(
                "1",
                "João",
                "12345678900",
                LocalDate.now().plusDays(1), // futuro válido
                List.of("Leite", "Pão"),
                true
        );
    }

    @Test
    void validoComDadosCorretos() {
        ColaboradorDto dto = buildValidDto();
        Set<ConstraintViolation<ColaboradorDto>> violations = validator.validate(dto);
        assertThat(violations).isEmpty();
    }

    @Test
    void validarNomeEmBranco() {
        ColaboradorDto dto = buildValidDto();
        dto = new ColaboradorDto(dto.id(), "", dto.cpf(), dto.dataCafe(), dto.itens(), dto.entregue());

        Set<ConstraintViolation<ColaboradorDto>> violations = validator.validate(dto);
        assertThat(violations)
                .anyMatch(v -> v.getPropertyPath().toString().equals("nome")
                        && v.getMessage().equals("O campo nome é obrigatório"));
    }

    @Test
    void validarCpfComFormatoIncorreto() {
        ColaboradorDto dto = buildValidDto();
        dto = new ColaboradorDto(dto.id(), dto.nome(), "abc123", dto.dataCafe(), dto.itens(), dto.entregue());

        Set<ConstraintViolation<ColaboradorDto>> violations = validator.validate(dto);
        assertThat(violations)
                .anyMatch(v -> v.getPropertyPath().toString().equals("cpf")
                        && v.getMessage().contains("O CPF deve conter apenas números"));
    }

    @Test
    void validarDataCafeNula() {
        ColaboradorDto dto = buildValidDto();
        dto = new ColaboradorDto(dto.id(), dto.nome(), dto.cpf(), null, dto.itens(), dto.entregue());

        Set<ConstraintViolation<ColaboradorDto>> violations = validator.validate(dto);
        assertThat(violations)
                .anyMatch(v -> v.getPropertyPath().toString().equals("dataCafe"));
    }

    @Test
    void validarDataCafeNoPassado() {
        ColaboradorDto dto = buildValidDto();
        dto = new ColaboradorDto(dto.id(), dto.nome(), dto.cpf(), LocalDate.now().minusDays(1), dto.itens(), dto.entregue());

        Set<ConstraintViolation<ColaboradorDto>> violations = validator.validate(dto);
        assertThat(violations)
                .anyMatch(v -> v.getPropertyPath().toString().equals("dataCafe")
                        && v.getMessage().contains("posterior à data atual"));
    }
}
