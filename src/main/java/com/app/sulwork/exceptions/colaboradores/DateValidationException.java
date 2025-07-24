package com.app.sulwork.exceptions.colaboradores;

import com.app.sulwork.exceptions.BaseException;

public class DateValidationException extends BaseException {
    public DateValidationException(String detail) {
        super("400", "Time validation error!", detail);
    }
}
