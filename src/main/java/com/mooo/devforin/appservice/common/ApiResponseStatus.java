package com.mooo.devforin.appservice.common;

import lombok.Getter;
import lombok.ToString;
import org.springframework.http.HttpStatus;

import java.util.Arrays;

@Getter
@ToString
public enum ApiResponseStatus {
	// 200
	SUCCESS(HttpStatus.OK, HttpStatus.OK.value(), "성공했습니다."),
	// --- 4xx Client Error ---
	BAD_REQUEST(HttpStatus.BAD_REQUEST, HttpStatus.BAD_REQUEST.value(), "잘못된 요청 입니다."),
	ACCESS_DENIED_EXCEPTION(HttpStatus.UNAUTHORIZED, HttpStatus.UNAUTHORIZED.value(), "인가된 사용자가 아닙니다."),
	// --- 5xx Server Error ---
	INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR.value(), HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase()),
	// customer
	AUTH_ERROR(HttpStatus.UNAUTHORIZED, HttpStatus.UNAUTHORIZED.value(), "인가된 사용자가 아닙니다."),
	SAVE_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR.value(), "저장시 내부 오류가 발생했습니다."),
	NOT_FOUND_ERROR(HttpStatus.OK, 204, "정보가 존재하지 않습니다."),
	USER_STTS_REG_ERROR(HttpStatus.OK, 204, "미등록 사용자 입니다."),
	ERROR_CASE_UNKNOWN(HttpStatus.OK,999,"무슨 에러일지 찾아야함")
	;


	private final HttpStatus status;
	private final int code;
	private String message;

	ApiResponseStatus(HttpStatus status, int code) {
		this.status = status;
		this.code = code;
	}

	ApiResponseStatus(HttpStatus status, int code, String message) {
		this.status = status;
		this.code = code;
		this.message = message;
	}

	public HttpStatus getStatus() {
		return this.status;
	}

	public int getCode() {
		return code;
	}

	public String getMessage() {
		return message;
	}


	public static ApiResponseStatus of(final int code){
		return Arrays.stream(values())
				.filter(val -> code == val.code)
				.findFirst()
				.orElse(null);
	}
}
