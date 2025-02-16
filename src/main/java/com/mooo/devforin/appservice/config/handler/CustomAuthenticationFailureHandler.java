package com.mooo.devforin.appservice.config.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mooo.devforin.appservice.config.global.ResponseUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import java.io.IOException;

// 로그인 실패 핸들러
@Slf4j
public class CustomAuthenticationFailureHandler implements AuthenticationFailureHandler {

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED); //401 인증 실패
        response.setContentType("application/json");

        log.error("Authentication failed: {}", exception.getMessage());  // 상세 오류 메시지 출력
        log.info("로그인 실패");
        response.getWriter().write(new ObjectMapper().writeValueAsString(ResponseUtil.FAILURE(401,"Authentication failed",exception.getMessage())));


    }
}
