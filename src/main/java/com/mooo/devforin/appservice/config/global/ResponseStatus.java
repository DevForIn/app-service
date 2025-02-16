package com.mooo.devforin.appservice.config.global;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ResponseStatus {

    // Success
    SUCCESS,
    FAILURE,
    ERROR;
}
