package com.mooo.devforin.appservice.config.handler;

import com.mooo.devforin.appservice.config.global.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class) // 모든 예외를 처리합니다.
    public ResponseEntity<ErrorResponse> handleAllExceptions(Exception ex) {

        ErrorResponse errorResponse = new ErrorResponse();

        errorResponse.setStatus("ERROR");
        errorResponse.setCode(500); //"에러 코드 ex) 500, 501..."
        errorResponse.setMessage("서버문제가 발생했습니다. 관리자에게 문의하세요."); // 사용자 메시지
        log.error(ex.getMessage());

        return new ResponseEntity<>(errorResponse, HttpStatus.valueOf(500));
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorResponse> handleTypeMismatchException(Exception ex) {

        ErrorResponse errorResponse = new ErrorResponse();

        errorResponse.setStatus("ERROR");
        errorResponse.setCode(400); //"에러 코드 ex) 500, 501..."
        errorResponse.setMessage("요청 데이터는 반드시 숫자형식이여야 합니다.");
        errorResponse.setData(ex.getMessage());

        log.error(ex.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.valueOf(400));
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> httpMessageNotReadableException(Exception ex) {

        ErrorResponse errorResponse = new ErrorResponse();

        errorResponse.setStatus("ERROR");
        errorResponse.setCode(400); //"에러 코드 ex) 500, 501..."
        errorResponse.setMessage("요청 JSON 데이터 확인이 필요합니다.");
        errorResponse.setData(ex.getMessage());

        log.error(ex.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.valueOf(400));
    }
}
