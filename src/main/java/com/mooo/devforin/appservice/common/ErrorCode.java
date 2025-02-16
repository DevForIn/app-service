package com.mooo.devforin.appservice.common;

import lombok.Getter;

@Getter
public enum ErrorCode {

    // 에러 코드와 메시지 정의 예시
    USER_NOT_FOUND("USER_001", "사용자를 찾을 수 없습니다."),
    INVALID_REQUEST("REQ_001", "요청이 유효하지 않습니다."),
    INTERNAL_SERVER_ERROR("SYS_001", "서버 내부 오류가 발생했습니다.");

    private final String code;
    private final String message;


    ErrorCode(String code, String message) {
        this.code = code;
        this.message = message;
    }
}
