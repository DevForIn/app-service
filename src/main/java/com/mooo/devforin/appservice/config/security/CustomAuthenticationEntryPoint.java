package com.mooo.devforin.appservice.config.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mooo.devforin.appservice.config.global.ResponseUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Slf4j
@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException)
            throws IOException, ServletException {

        // 401 상태 코드와 에러 메시지 설정
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");

        // JSON 형식으로 에러 메시지 반환
        log.error("🚨 인증 실패: 요청 URL = {}, 메세지 = {}", request.getRequestURI(), authException.getMessage());
        response.getWriter().write(new ObjectMapper().writeValueAsString(ResponseUtil.ERROR(401,"Authentication failed", authException.getMessage())));
    }
}
