package com.mooo.devforin.appservice.config.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mooo.devforin.appservice.config.global.ResponseUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException)
            throws IOException, ServletException {

        // 403 상태 코드와 에러 메시지 설정
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        response.setContentType("application/json");

        // JSON 형식으로 에러 메시지 반환
        response.getWriter().write(new ObjectMapper().writeValueAsString(ResponseUtil.FAILURE(403,"Access Denied", accessDeniedException.getMessage())));
    }
}
