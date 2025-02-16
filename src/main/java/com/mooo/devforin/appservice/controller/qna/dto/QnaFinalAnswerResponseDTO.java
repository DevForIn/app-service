package com.mooo.devforin.appservice.controller.qna.dto;

import lombok.Data;
import java.util.List;

/**
 * PLUGNET Workflow(최종답변) 응답 Dto
 */
@Data
public class QnaFinalAnswerResponseDTO {
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
        private String __error_code__;
        private String __error_message__;
        private String __id__;
        private String __status__;
        private String totalAnswerResult;
        private String multiturnResult;
    }
}