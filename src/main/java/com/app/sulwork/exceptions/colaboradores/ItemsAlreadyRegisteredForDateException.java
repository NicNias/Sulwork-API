package com.app.sulwork.exceptions.colaboradores;

import com.app.sulwork.exceptions.BaseException;

public class ItemsAlreadyRegisteredForDateException extends BaseException {
    public ItemsAlreadyRegisteredForDateException(String detail) {
        super("400", "Item ja cadastrado para está data!", detail);
    }
}
