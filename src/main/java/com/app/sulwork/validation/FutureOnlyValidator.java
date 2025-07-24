package com.app.sulwork.validation;

import com.app.sulwork.exceptions.DateValidationException;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.LocalDate;

public class FutureOnlyValidator implements ConstraintValidator<FutureOnly, LocalDate> {

    @Override
    public boolean isValid(LocalDate date, ConstraintValidatorContext context) {
        if (date == null) {
            return false;
        }

        return LocalDate.now().isBefore(date);
    }
}