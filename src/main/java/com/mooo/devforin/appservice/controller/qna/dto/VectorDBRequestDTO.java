package com.mooo.devforin.appservice.controller.qna.dto;

import lombok.Builder;
import lombok.Data;

/**
 * Vector DB (문서 출처) 요청 Dto
 */
@Data
public class VectorDBRequestDTO {
    private String filter;
    private String select;

    @Builder
    public VectorDBRequestDTO(String filter, String select) {
        this.filter = filter;
        this.select = select;
    }
}
