package com.mooo.devforin.appservice.controller.question.dto;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Data
@NoArgsConstructor // 기본 생성자 추가
public class QuestionRequestDto {

    private String q;

    @Builder
    public QuestionRequestDto(String q) {
        this.q = q;
    }
}
