package com.mooo.devforin.appservice.config.global;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor

public class ResponseDTO<T> {
    private final ResponseStatus status;
    private final int code;
    private final String message;
    private final T data;
}