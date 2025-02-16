package com.mooo.devforin.appservice.controller.qna.dto;

import lombok.Builder;
import lombok.Data;

import java.sql.Timestamp;

/**
 * 심사 Assistant 이전 질문 Dto
 */
@Data
public class QnaChatListDTO {
    private Integer chatSn;
    private Integer historySn;
    private String userQuery;
    private Timestamp createdAt;
    private Timestamp updatedAt;

    @Builder
    public QnaChatListDTO(Integer chatSn, Integer historySn, String userQuery, Timestamp createdAt, Timestamp updatedAt) {
        this.chatSn = chatSn;
        this.historySn = historySn;
        this.userQuery = userQuery;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
}
