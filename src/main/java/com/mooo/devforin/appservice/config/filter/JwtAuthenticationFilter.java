package com.mooo.devforin.appservice.config.filter;

import com.mooo.devforin.appservice.config.global.CustomUserDetails;
import com.mooo.devforin.appservice.service.authorization.impl.UserDetailsServiceImpl;
import com.mooo.devforin.appservice.util.JwtUtil;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;


@Component
@Slf4j
//요청당 한 번 실행되는 필터로, Spring Security에서 제공하는 기본 필터 클래스
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    // JWT 유틸리티 클래스 주입
    @Autowired
    private JwtUtil jwtUtil;

    // 사용자 정보 불러오는 서비스
    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {

        // 요청 헤더에서 Authorization 헤더 추출
        final String authorizationHeader = request.getHeader("Authorization");

        try {
            if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
                String jwt = authorizationHeader.substring(7);
                String id = jwtUtil.extractId(jwt);

                if (id != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                    CustomUserDetails userDetails = userDetailsService.loadUserByUsername(id);

                    // JWT가 유효한지 확인
                    if (jwtUtil.validateToken(jwt, userDetails)) {
                        // 인증 객체 생성 및 SecurityContext에 설정
                        UsernamePasswordAuthenticationToken authenticationToken =
                                new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                        authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                        request.setAttribute("token",jwt);
                        log.info("Successfully authenticated id: {}", id);
                    }
                }
            }
        }catch (MalformedJwtException e) {
            log.error("Invalid JWT token: {}", e.getMessage());
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid JWT token");
            return;
        } catch (ExpiredJwtException e) {
            log.error("JWT token has expired: {}", e.getMessage());
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "JWT token has expired");
            return;
        } catch (SignatureException e) {
            log.error("Invalid JWT signature: {}", e.getMessage());
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid JWT signature");
            return;
        } catch (Exception e) {
            log.error("JWT token processing failed: {}", e.getMessage());
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");
            return;
        }


        // 요청을 다음 필터로 전달
        chain.doFilter(request, response);
    }
}
