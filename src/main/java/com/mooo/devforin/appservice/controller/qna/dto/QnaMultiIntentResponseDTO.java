package com.mooo.devforin.appservice.controller.qna.dto;

import lombok.Data;

import java.util.List;

/**
 * PLUGNET Workflow(멀티인텐트) 요청 Dto
 */
@Data
public class QnaMultiIntentResponseDTO {
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

    // TODO - 24/12/05 고객 Test 후 finalResult 복구
    
    @lombok.Data
    public static class Data {
        private String __error_message__;
        private String __error_code__;
        private String __id__;
        private String __status__;
//        private List<FinalResult> finalResult;
        private String finalResult;
    }

//    @lombok.Data
//    public static class FinalResult{
//        private String question;
//    }
}