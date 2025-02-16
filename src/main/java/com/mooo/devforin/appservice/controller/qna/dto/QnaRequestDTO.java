package com.mooo.devforin.appservice.controller.qna.dto;

import lombok.Builder;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 *  Q&A 문의하기, Assistant 요청 Dto
 */
@Data
public class QnaRequestDTO implements Serializable {

    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
    private static final AtomicInteger SERIAL_NUMBER = new AtomicInteger(0);

    @Serial
    private static final long serialVersionUID = 3095549166280858813L;

    private Integer chatSn;
    private String sessionId;
    private List<Input> input;

    @Data
    public static class Input{
        public String user_query;
        public List<MultiTurnHistoryDTO> CHAT_HIS;
        public String isAnswer;
        public List<Answer> answer;
        public String disease_id;
        public String manual_id;

        @Data
        public static class Answer{
            public String answer_query;

            @Builder
            public Answer(String answer_query) {
                this.answer_query = answer_query;
            }
        }

        @Builder
        public Input(String user_query, List<MultiTurnHistoryDTO> CHAT_HIS, String isAnswer, List<Answer> answer, String disease_id, String manual_id) {
            this.user_query = user_query;
            this.CHAT_HIS = CHAT_HIS;
            this.isAnswer = isAnswer;
            this.answer = answer;
            this.disease_id = disease_id;
            this.manual_id = manual_id;
        }
    }

    @Builder
    public QnaRequestDTO(String sessionId, List<Input> input) {
        this.sessionId = sessionId;
        this.input = input;
    }
}
