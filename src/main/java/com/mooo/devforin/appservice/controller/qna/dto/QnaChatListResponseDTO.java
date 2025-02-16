package com.mooo.devforin.appservice.controller.qna.dto;

import lombok.Data;

import java.util.List;

/**
 *  심사 Assistant 이전 질문 목록 Dto
 */
@Data
public class QnaChatListResponseDTO {
    private Boolean dataFlag;
    private List<QnaChatListDTO> ChatHistoryList;

    public QnaChatListResponseDTO(Boolean dataFlag, List<QnaChatListDTO> ChatHistoryList) {
        this.dataFlag = dataFlag;
        this.ChatHistoryList = ChatHistoryList;
    }
}
