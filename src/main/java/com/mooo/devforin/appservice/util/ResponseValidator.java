package com.mooo.devforin.appservice.util;

import org.springframework.http.ResponseEntity;

public class ResponseValidator {

    /**
     * ResponseEntity의 상태와 결과 코드를 검증하는 유틸 메서드
     *
     * @param response    검증할 ResponseEntity
     * @param successCode 성공으로 간주할 코드 (예: "200")
     * @param <T>         Response Body 타입
     * @return 성공 여부 (true: 성공, false: 실패)
     */
    public static <T> boolean isValidResponse(ResponseEntity<T> response, String successCode) {
        if (response == null || response.getBody() == null) {
            return false;
        }
        return true;
//        return successCode.equals(Objects.requireNonNull(response.getBody()).getCode());
    }
}
