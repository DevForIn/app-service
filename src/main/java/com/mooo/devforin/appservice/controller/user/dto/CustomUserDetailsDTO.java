package com.mooo.devforin.appservice.controller.user.dto;

import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Data
public class CustomUserDetailsDTO {

    /**
     * 로그인 아이디
     */
    private final String id;

    /**
     * 접속 아이디
     */
    private final String username;

    /**
     * auth token
     */
    private final String token;

    /**
     * token 만료 시간(Data타입)
     */
    private final Date credentialsExpiredDate;

    /**
     * token 만료 여부 (true/false)
     */
    private final boolean credentialsNonExpired;

    @Builder
    public CustomUserDetailsDTO(String id, String username, String token, Date credentialsExpiredDate, boolean credentialsNonExpired) {
        this.id = id;
        this.username = username;
        this.token = token;
        this.credentialsExpiredDate = credentialsExpiredDate;
        this.credentialsNonExpired = credentialsNonExpired;
    }
}
