package com.app.sulwork.exceptions.colaboradores;

import com.app.sulwork.exceptions.BaseException;

public class ColaboradoresNotFoundException extends BaseException {
    public ColaboradoresNotFoundException(String detail) {
        super("404", "Colaboradores não encontrados na base de dados!", detail);
    }
}
