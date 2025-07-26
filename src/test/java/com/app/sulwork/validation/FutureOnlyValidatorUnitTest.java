package com.app.sulwork.validation;

import jakarta.validation.ConstraintValidatorContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

class FutureOnlyValidatorUnitTest {
    private FutureOnlyValidator validator;
    private ConstraintValidatorContext context;

    @BeforeEach
    void setUp() {
        validator = new FutureOnlyValidator();
        context = mock(ConstraintValidatorContext.class);
    }

    @Test
    void isValid_retornaTrue_quandoDataNula() {
        assertThat(validator.isValid(null, context)).isTrue();
    }

    @Test
    void isValid_retornaTrue_quandoDataNoFuturo() {
        LocalDate dataFutura = LocalDate.now().plusDays(1);
        assertThat(validator.isValid(dataFutura, context)).isTrue();
    }

    @Test
    void isValid_retornaFalse_quandoDataIgualOuPassada() {
        LocalDate dataHoje = LocalDate.now();
        LocalDate dataPassada = LocalDate.now().minusDays(1);

        assertThat(validator.isValid(dataHoje, context)).isFalse();
        assertThat(validator.isValid(dataPassada, context)).isFalse();
    }
}
