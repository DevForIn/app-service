package com.mooo.devforin.appservice.controller.qna.dto;

import lombok.Builder;
import lombok.Data;

import java.sql.Timestamp;

/**
 * 피드백 상세 조회 Dto
 */
@Data
public class FeedbackViewResponseDTO
{
    private int historySn;
    private int feedbackSn;
    private String feedbackInfo;
    private String referenceInfo;
    private String createdBy;
    private Timestamp createdAt;

    @Builder
    public FeedbackViewResponseDTO(int historySn, int feedbackSn, String feedbackInfo, String referenceInfo, String createdBy, Timestamp createdAt) {
        this.historySn = historySn;
        this.feedbackSn = feedbackSn;
        this.feedbackInfo = feedbackInfo;
        this.referenceInfo = referenceInfo;
        this.createdBy = createdBy;
        this.createdAt = createdAt;
    }
}
