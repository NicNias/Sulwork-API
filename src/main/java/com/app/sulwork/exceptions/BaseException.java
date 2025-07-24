package com.app.sulwork.exceptions;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class BaseException extends RuntimeException {
    private final String httpStatusCode;
    private final String title;
    private final String detail;
}
