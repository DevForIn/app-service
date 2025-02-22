package com.mooo.devforin.appservice.controller.user;

import com.mooo.devforin.appservice.config.global.ResponseDTO;
import com.mooo.devforin.appservice.config.global.ResponseUtil;
import com.mooo.devforin.appservice.controller.user.dto.UserJoinInfoDTO;
import com.mooo.devforin.appservice.domain.entity.AdminUsers;
import com.mooo.devforin.appservice.service.authorization.impl.UserDetailsServiceImpl;
import com.mooo.devforin.appservice.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;


@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/user")
public class UserController {

    private final UserDetailsServiceImpl userDetailsService;

    private final JwtUtil jwtUtil;

    @PostMapping("/insert-user")
    public ResponseDTO insertUser(@RequestBody UserJoinInfoDTO dto){
        AdminUsers adminUsers = userDetailsService.insertUser(dto);

        if(adminUsers == null){
            return ResponseUtil.ERROR(400,"User 생성 실패",null);
        }else {
            return ResponseUtil.SUCCESS(201,"User 생성 성공","ID : "+adminUsers.getId());
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
        Map<String, Object> claims = new HashMap<>();

        String token = jwtUtil.createToken(claims, id);

        return ResponseUtil.SUCCESS(200,"Token 생성 성공",token);
    }
}
