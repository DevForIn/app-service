package com.mooo.devforin.appservice.config.security;

import com.mooo.devforin.appservice.config.filter.CustomAuthenticationFilter;
import com.mooo.devforin.appservice.config.filter.JwtAuthenticationFilter;
import com.mooo.devforin.appservice.config.filter.XssFilter;
import com.mooo.devforin.appservice.config.handler.CustomAccessDeniedHandler;
import com.mooo.devforin.appservice.config.handler.CustomAuthenticationFailureHandler;
import com.mooo.devforin.appservice.config.handler.CustomAuthenticationSuccessHandler;
import com.mooo.devforin.appservice.util.JwtUtil;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;


@Configuration
@EnableWebSecurity
public class SecurityConfig{
    // JWT 필터 의존성 주입
    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final JwtUtil jwtUtil;


    // SecurityConfig 생성자 - JWT 필터 주입
    public SecurityConfig(JwtAuthenticationFilter jwtAuthenticationFilter, JwtUtil jwtUtil) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
        this.jwtUtil = jwtUtil;
    }

    // AuthenticationManager Bean 생성
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    // 비밀번호 암호화에 사용할 BCrypt 인코더 생성
    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    // 인증 성공 핸들러
    @Bean
    public AuthenticationSuccessHandler successHandler(JwtUtil jwtUtil){
        return new CustomAuthenticationSuccessHandler(jwtUtil);
    }

    // 인증 실패 핸들러
    @Bean
    public AuthenticationFailureHandler failureHandler(){
        return new CustomAuthenticationFailureHandler();
    }

    @Bean
    protected SecurityFilterChain configure(HttpSecurity http) throws Exception {

        // AuthenticationManager 필요
        AuthenticationManager authenticationManager = authenticationManager(http.getSharedObject(AuthenticationConfiguration.class));

        // CustomAuthenticationFilter 생성
        CustomAuthenticationFilter customAuthenticationFilter = new CustomAuthenticationFilter(
                authenticationManager, successHandler(jwtUtil), failureHandler()
        );
        customAuthenticationFilter.setFilterProcessesUrl("/auth/login"); // 로그인 경로 설정


        http
                .csrf(csrf -> csrf.disable())   // CSRF(Cross-Site Request Forgery) 보호 비활성화
//                .cors(cors -> cors.configurationSource(corsConfigurationSource())) // CORS 설정 추가
                .authorizeHttpRequests(auth ->
                                auth
//                                    .requestMatchers("/user/test","/user/join","/auth/login","/v1/assistant/*").permitAll()     // "/login", "/auth/login" 경로는 인증 없이 접근 허용
                                    .requestMatchers("/v1/user/**").permitAll()
//                                    .requestMatchers("/**").permitAll()
                                    .anyRequest().authenticated()       // 그 외 모든 요청은 인증 필요
                )
//                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // 세션 관리 비활성화(JWT 사용을 위한 무상태 설정)
                .exceptionHandling(exception -> exception
                        .authenticationEntryPoint(new CustomAuthenticationEntryPoint())  // 인증 실패 시 처리
                        .accessDeniedHandler(new CustomAccessDeniedHandler())  // 권한 거부 시 처리
                )
                .addFilterBefore(new XssFilter(), UsernamePasswordAuthenticationFilter.class)  // XSS 필터 추가
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)  // JWT 필터 추가
                .addFilterAfter(customAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);// 커스텀 로그인 필터 추가

        return http.build();
    }

    // CORS 설정
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("*")); // 허용할 출처 설정
        configuration.setAllowedMethods(Arrays.asList("*")); // 허용할 HTTP 메서드
        configuration.setAllowCredentials(true); // 자격 증명 허용
        configuration.setAllowedHeaders(Arrays.asList("*")); // 허용할 헤더 설정

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration); // 모든 경로에 대해 CORS 설정 적용
        return source;
    }
}

