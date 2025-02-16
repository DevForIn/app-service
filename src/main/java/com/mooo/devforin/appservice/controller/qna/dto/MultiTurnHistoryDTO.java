package com.mooo.devforin.appservice.controller.qna.dto;

import lombok.Builder;
import lombok.Data;

/**
 *  멀티턴용 대화 정보 Dto
 */
@Data
public class MultiTurnHistoryDTO {
    private String role;
    private String content;

    @Builder
    public MultiTurnHistoryDTO(String role, String content) {
        this.role = role;
        this.content = content;
    }
}
