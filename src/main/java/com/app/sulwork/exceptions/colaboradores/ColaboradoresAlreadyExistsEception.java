package com.app.sulwork.exceptions.colaboradores;

import com.app.sulwork.exceptions.BaseException;

public class ColaboradoresAlreadyExistsEception extends BaseException {
    public ColaboradoresAlreadyExistsEception(String detail) {
        super("400", "Colaborador ja cadastrado!", detail);
    }
}
