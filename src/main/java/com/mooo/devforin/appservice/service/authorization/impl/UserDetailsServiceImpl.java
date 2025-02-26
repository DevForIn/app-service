package com.mooo.devforin.appservice.service.authorization.impl;

import com.mooo.devforin.appservice.config.global.CustomUserDetails;
import com.mooo.devforin.appservice.controller.user.dto.UserJoinInfoDTO;
import com.mooo.devforin.appservice.domain.entity.User;
import com.mooo.devforin.appservice.domain.repository.user.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    @Transactional
    public CustomUserDetails loadUserByUsername(String id) throws UsernameNotFoundException {

        // DB에서 사용자 이름으로 사용자 검색
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with id: " + id));

        // 기타 필드 설정
        List<GrantedAuthority> authorities = List.of(new SimpleGrantedAuthority("ROLE_USER"));

        return new CustomUserDetails(user.getId(), user.getUsername(), user.getPassword(), authorities);  // 사용자 정보를 UserDetails 객체로 반환
    }

    @Transactional
    public User insertUser(UserJoinInfoDTO dto) {

        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String encodedPassword = passwordEncoder.encode(dto.getPassword());

        return userRepository.save(User.builder()
                                            .id(dto.getId())
                                            .password(encodedPassword)
                                            .username(dto.getUsername())
                                            .build());
    }
}

