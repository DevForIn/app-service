package com.mooo.devforin.appservice.config.global;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ErrorResponse {
    private String status;
    private int code;
    private String message;
    private String data = null;
}