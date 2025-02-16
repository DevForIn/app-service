package com.mooo.devforin.appservice.controller.qna.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

/**
 *  Vector DB (문서 출처) 응답 Dto
 */
@Data
public class VectorDBResponseDTO {

    @JsonProperty("@odata.context")
    private String odata_context;
    private List<Doc> value;

    @lombok.Data
    public static class Doc {
        @JsonProperty("@search.score")
        private double search_score;
        private String id;
        private String doc_title;
        private String content_for_rag;
    }
}
