package com.mooo.devforin.appservice.util;

import com.mooo.devforin.appservice.config.global.CustomUserDetails;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component
public class JwtUtil {

    // 토큰 서명에 사용할 비밀키
    @Value("${token.secret}")
    private String SECRET_KEY;

    @Value("${token.expire.time}")
    private int TOKEN_TIME;

    // 비밀 키를 SecretKey 객체로 생성
    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(SECRET_KEY.getBytes());
    }

    // 토큰에서 사용자 이름 추출
    public String extractId(String token){
        return extractClaim(token, Claims::getSubject);
    }

    // 토큰 만료 시간 추출
    public Date extractExpiration(String token){
        return extractClaim(token,Claims::getExpiration);
    }

    
    // 토큰에서 특정클레임 추출
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver){
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    // 토큰에서 모든 클레임 파싱
    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)  // 여기를 parseClaimsJws로 수정
                .getBody();
    }

    // 토큰 만료 여부 확인
    private Boolean isTokenExpired(String token){
        return extractExpiration(token).before(new Date());
    }


    // 사용자 정보 기반으로 토큰 생성
    public String generateToken(CustomUserDetails userDetails){
        Map<String, Object> claims = new HashMap<>();
        return createToken(claims,userDetails.getId());
    }

    public String createToken(Map<String,Object> claims, String subject){
        return Jwts.builder()
                .setClaims(claims)      // 클레임 설정
                .setSubject(subject)    // 사용자 이름 설정
                .setIssuedAt(new Date(System.currentTimeMillis())) // 토큰 발급 시간 설정
                .setExpiration(new Date(System.currentTimeMillis() + TOKEN_TIME)) // 토큰 만료 시간 설정
                .signWith(getSigningKey(), SignatureAlgorithm.HS256) // 비밀키 사용 서명
                .compact(); // 토큰 생성
    }

    // 토큰 유효 확인
    public boolean validateToken(String token, CustomUserDetails userDetails){
        final String id = extractId(token);
        return (id.equals(userDetails.getId()) && !isTokenExpired(token));
    }
}
