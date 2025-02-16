package com.mooo.devforin.appservice.controller.qna.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

import java.util.List;

/**
 * PLUGNET Workflow(Process) 요청 Dto
 */
@Data
public class QnaAnswerRequestDTO {

    private String sessionId;
    private List<Input> input;

    @Data
    public static class Input{
        @JsonProperty("user_query")
        private String user_query;
        private String diseaseName;
        private String productName;
        private String top_k;
        private String today_date;

        @Builder
        public Input(String user_query, String diseaseName, String productName, String top_k, String today_date) {
            this.user_query = user_query;
            this.diseaseName = diseaseName;
            this.productName = productName;
            this.top_k = top_k;
            this.today_date = today_date;
        }
    }

    @Builder
    public QnaAnswerRequestDTO(String sessionId, List<Input> input) {
        this.sessionId = sessionId;
        this.input = input;
    }
}
