package com.app.sulwork.validation;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import jakarta.validation.ConstraintValidatorContext;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CpfValidatorUnitTest {
    private CpfValidator validator;

    @BeforeEach
    void setUp() {
        validator = new CpfValidator();
    }

    @Test
    void testValidCpf() {
        // CPF válido: 52998224725
        assertTrue(validator.isValid("52998224725", mockContext()));
    }

    @Test
    void testInvalidCpf_WrongDigits() {
        // CPF com dígitos verificadores errados
        assertFalse(validator.isValid("12345678900", mockContext()));
    }

    @Test
    void testInvalidCpf_RepeatedDigits() {
        // CPF inválido: todos os dígitos iguais
        assertFalse(validator.isValid("11111111111", mockContext()));
    }

    @Test
    void testInvalidCpf_ShortLength() {
        // CPF com menos de 11 dígitos
        assertFalse(validator.isValid("12345678", mockContext()));
    }

    @Test
    void testInvalidCpf_Null() {
        // CPF nulo
        assertFalse(validator.isValid(null, mockContext()));
    }

    @Test
    void testInvalidCpf_ContainsLetters() {
        // CPF com letras
        assertFalse(validator.isValid("abc12345678", mockContext()));
    }

    private ConstraintValidatorContext mockContext() {
        return mock(ConstraintValidatorContext.class);
    }
}
