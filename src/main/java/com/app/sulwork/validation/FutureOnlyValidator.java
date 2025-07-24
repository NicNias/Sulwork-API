package com.app.sulwork.validation;

import com.app.sulwork.exceptions.colaboradores.DateValidationException;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.LocalDate;

public class FutureOnlyValidator implements ConstraintValidator<FutureOnly, LocalDate> {

    @Override
    public boolean isValid(LocalDate date, ConstraintValidatorContext context) {
        // Return 'true' vai passar a responsabilidade de validação de nullos
        // para a annotation @NotNull, dividindo as responsabilidades e seguindo os padrões SOLID.
        if(date == null) {
            return true;
        }

        return LocalDate.now().isBefore(date);
    }
}