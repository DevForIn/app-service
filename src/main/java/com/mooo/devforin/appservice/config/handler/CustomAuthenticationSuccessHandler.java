package com.mooo.devforin.appservice.config.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mooo.devforin.appservice.config.global.ResponseUtil;
import com.mooo.devforin.appservice.config.global.CustomUserDetails;
import com.mooo.devforin.appservice.controller.user.dto.CustomUserDetailsDTO;
import com.mooo.devforin.appservice.util.JwtUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import java.io.IOException;

// 로그인 성공 핸들러
@Slf4j
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    private final JwtUtil jwtUtil;

    public CustomAuthenticationSuccessHandler(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }


    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {

        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();

        // JWT 토큰 생성
        String token = jwtUtil.generateToken(userDetails);

        // 응답 헤더에 JWT 토큰 추가 (Bearer 스킴 사용)
        response.addHeader("Authorization", "Bearer " + token);

        // 응답 본문 생성
        CustomUserDetailsDTO customUserDetailsDto = CustomUserDetailsDTO.builder()
                .token(token)
                .id(jwtUtil.extractId(token))
                .username(userDetails.getUsername())
                .credentialsExpiredDate(jwtUtil.extractExpiration(token))
                .credentialsNonExpired(jwtUtil.validateToken(token, userDetails))
                .build();


        // JSON 형식으로 응답
        response.setContentType("application/json");
        response.getWriter().write(new ObjectMapper().writeValueAsString(ResponseUtil.SUCCESS(200,"Authorization Success", customUserDetailsDto)));
    }
}
