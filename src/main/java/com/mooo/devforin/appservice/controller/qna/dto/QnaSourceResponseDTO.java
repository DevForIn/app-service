package com.mooo.devforin.appservice.controller.qna.dto;

import lombok.Builder;
import lombok.Data;

/**
 *  Q&A 문의하기, Assistant 응답 정보의 문서 출처 정보 Dto
 */
@Data
public class QnaSourceResponseDTO {
    private String title;
    private String intent;
    private String chunkName;
    private String content;

    @Builder
    public QnaSourceResponseDTO(String title, String intent, String chunkName, String content) {
        this.title = title;
        this.intent = intent;
        this.chunkName = chunkName;
        this.content = content;
    }
}
