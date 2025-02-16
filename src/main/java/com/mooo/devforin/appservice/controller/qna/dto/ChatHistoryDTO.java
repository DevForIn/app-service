package com.mooo.devforin.appservice.controller.qna.dto;

import lombok.Builder;
import lombok.Data;

import java.sql.Timestamp;
import java.util.List;

/**
 *  최종 답변의 chatHistory Dto
 */
@Data
public class ChatHistoryDTO
{
    private int historySn;
    private String userQuery;
    private String answer;
    private Integer feedbackSn;
    private String answerRevaluation;
    private Timestamp createdAt;
    private List<QnaSourceResponseDTO> source;

    @Builder
    public ChatHistoryDTO(int historySn, String userQuery, String answer, Integer feedbackSn, String answerRevaluation, Timestamp createdAt, List<QnaSourceResponseDTO> source) {
        this.historySn = historySn;
        this.userQuery = userQuery;
        this.answer = answer;
        this.feedbackSn = feedbackSn;
        this.answerRevaluation = answerRevaluation;
        this.createdAt = createdAt;
        this.source = source;
    }
}
