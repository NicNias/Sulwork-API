package com.app.sulwork.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Constraint(validatedBy = FutureOnlyValidator.class)
@Target({ElementType.PARAMETER, ElementType.FIELD, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface FutureOnly {
    String message() default "A data deve ser futura (não pode ser hoje nem passada)";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
