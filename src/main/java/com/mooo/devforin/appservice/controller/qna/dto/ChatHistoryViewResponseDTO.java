package com.mooo.devforin.appservice.controller.qna.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

/**
 *  최종 답변 Dto
 */
@Data
public class ChatHistoryViewResponseDTO
{
    private String sessionId;
    private boolean multiTurnStatus;
    private List<ChatHistoryDTO> historys;

    @Builder
    public ChatHistoryViewResponseDTO(String sessionId, boolean multiTurnStatus, List<ChatHistoryDTO> historys) {
        this.sessionId = sessionId;
        this.multiTurnStatus = multiTurnStatus;
        this.historys = historys;
    }
}
