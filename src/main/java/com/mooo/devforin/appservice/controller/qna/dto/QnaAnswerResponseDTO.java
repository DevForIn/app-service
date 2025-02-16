package com.mooo.devforin.appservice.controller.qna.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

/**
 * PLUGNET Workflow(Process) 응답 Dto
 */
@Data
public class QnaAnswerResponseDTO {
    private String code;
    private String message;
    private String sessionId;
    private String requestId;
    private Result result;

    @lombok.Data
    public static class Result {
        private List<Data> data;
        private int request;
        private int success;
        private int failure;
    }

    @lombok.Data
    public static class Data {
        private String __error_message__;
        private String __error_code__;
        private String __id__;
        private String __status__;
        private String user_query;
        private String productIntent;
        private String diseaseIntent;
        private DocResult docResult;
        private String llmResult;

    }

    @lombok.Data
    public static class DocResult{
        @JsonProperty("@odata.context")
        private String odata_context;
        @JsonProperty("@odata.count")
        private int  odata_count;
        private List<Doc> value;


    }

    @lombok.Data
    public static class Doc {
        @JsonProperty("@search.score")
        private double search_score;
        private String id;
        private String doc_title;
        private String chunk_name;
        private String doc_type;
        private String content_for_rag;

    }
}