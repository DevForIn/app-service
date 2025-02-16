package com.mooo.devforin.appservice.config.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mooo.devforin.appservice.controller.user.dto.UserInfoDTO;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import java.io.IOException;

public class CustomAuthenticationFilter extends AbstractAuthenticationProcessingFilter {
    private final AuthenticationSuccessHandler successHandler;
    private final AuthenticationFailureHandler failureHandler;


    public CustomAuthenticationFilter(AuthenticationManager authenticationManager,
                                      AuthenticationSuccessHandler successHandler,
                                      AuthenticationFailureHandler failureHandler) {
        super("/auth/login"); // 로그인 경로 설정
        setAuthenticationManager(authenticationManager);
        this.successHandler = successHandler;
        this.failureHandler = failureHandler;
    }

    @Override
    protected boolean requiresAuthentication(HttpServletRequest request, HttpServletResponse response) {
        return "POST".equalsIgnoreCase(request.getMethod()) && super.requiresAuthentication(request, response);
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException {
        // JSON으로 전달된 데이터를 처리
        ObjectMapper objectMapper = new ObjectMapper();
        UserInfoDTO userInfoDto;

        try {
            // request의 body에서 UserInfoDto 객체로 변환
            userInfoDto = objectMapper.readValue(request.getInputStream(), UserInfoDTO.class);
        } catch (IOException e) {
            throw new RuntimeException("Failed to read request body", e);
        }

        String id = userInfoDto.getId();
        String password = userInfoDto.getPassword();


        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(id, password);

        // AuthenticationManager에 의해 인증 처리
        return getAuthenticationManager().authenticate(authenticationToken);
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
                                            Authentication authResult) throws IOException, ServletException {
        // 인증 성공 시 성공 핸들러 호출
        successHandler.onAuthenticationSuccess(request, response, authResult);
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response,
                                              AuthenticationException failed) throws IOException, ServletException {
        // 인증 실패 시 실패 핸들러 호출
        failureHandler.onAuthenticationFailure(request, response, failed);
    }
}
