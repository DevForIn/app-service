package com.mooo.devforin.appservice.controller.user;

import com.mooo.devforin.appservice.config.global.CustomUserDetails;
import com.mooo.devforin.appservice.config.global.ResponseDTO;
import com.mooo.devforin.appservice.config.global.ResponseUtil;
import com.mooo.devforin.appservice.controller.user.dto.UserJoinInfoDTO;
import com.mooo.devforin.appservice.domain.entity.User;
import com.mooo.devforin.appservice.service.authorization.impl.UserDetailsServiceImpl;
import com.mooo.devforin.appservice.util.JwtUtil;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;


@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/user")
public class UserController {

    private final UserDetailsServiceImpl userDetailsService;

    private final JwtUtil jwtUtil;

    @PostMapping("/insert-user")
    public ResponseDTO insertUser(@RequestBody UserJoinInfoDTO dto){
        User user = userDetailsService.insertUser(dto);

        if(user == null){
            return ResponseUtil.ERROR(400,"User 생성 실패",null);
        }else {
            return ResponseUtil.SUCCESS(201,"User 생성 성공","ID : "+ user.getId());
        }
    }

    /**
     * TODO - TEST
     *
     * @param id
     * @return
     */
    @Deprecated
    @GetMapping("/getToken/{id}")
    public ResponseDTO getToken(@PathVariable("id") String id){

        // 인가/인증을 통하여 접근한 사용자 정보 = getPrincipal();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();

        // JwtFilter에서 HttpServletRequest 에 token이라는 Attribute를 추가하여 사용자 token 정보 get
        ServletRequestAttributes attrs = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attrs.getRequest();
        String token = (String) request.getAttribute("token");

        // 인증 유효 시간 : 2주
        log.info("Token time is : {}",jwtUtil.extractExpiration(token));
        // 인가/인증한 사용자 id
        log.info("extractClaim is : {}",jwtUtil.extractClaim(token, Claims::getSubject));
        // 해당 token validation
        log.info("validateToken -> {}",jwtUtil.validateToken(token,customUserDetails));

        return ResponseUtil.SUCCESS(200,"Token 생성 성공",null);
    }
}
