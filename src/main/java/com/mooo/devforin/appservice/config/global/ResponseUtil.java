package com.mooo.devforin.appservice.config.global;


public class ResponseUtil {

    public static <T> ResponseDTO<T> SUCCESS (int code, String message, T data) {
        return new ResponseDTO(ResponseStatus.SUCCESS, code, message, data);
    }

    public static <T> ResponseDTO<T> FAILURE (int code, String message, T data) {
        return new ResponseDTO(ResponseStatus.FAILURE,code , message, data);
    }

    public static <T> ResponseDTO<T> ERROR (int code, String message, T data) {
        return new ResponseDTO(ResponseStatus.ERROR, code,message, data);
    }
}