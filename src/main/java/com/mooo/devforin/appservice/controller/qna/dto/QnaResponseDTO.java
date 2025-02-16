package com.mooo.devforin.appservice.controller.qna.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

/**
 *  Q&A 문의하기, Assistant 응답 Dto
 */
@Data
public class QnaResponseDTO {
    private String code;
    private String message;
    private String sessionId;
    private String requestId;
    private String dataId;
    private int chatSn;
    private int historySn;
    private boolean multiTurnStatus;
    private String totalAnswerResult;
    private String multiturnResult;
    private List<QnaSourceResponseDTO> Source;

    @Builder
    public QnaResponseDTO(String code, String message, String sessionId, String requestId, String dataId, int chatSn, int historySn, boolean multiTurnStatus, String totalAnswerResult, String multiturnResult, List<QnaSourceResponseDTO> source) {
        this.code = code;
        this.message = message;
        this.sessionId = sessionId;
        this.requestId = requestId;
        this.dataId = dataId;
        this.chatSn = chatSn;
        this.historySn = historySn;
        this.multiTurnStatus = multiTurnStatus;
        this.totalAnswerResult = totalAnswerResult;
        this.multiturnResult = multiturnResult;
        Source = source;
    }
}