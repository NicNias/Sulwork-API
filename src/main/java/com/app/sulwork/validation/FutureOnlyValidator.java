package com.app.sulwork.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.LocalDate;

public class FutureOnlyValidator implements ConstraintValidator<FutureOnly, LocalDate> {

    @Override
    public boolean isValid(LocalDate date, ConstraintValidatorContext context) {
        if (date == null) return true;
        return date.isAfter(LocalDate.now());
    }
}