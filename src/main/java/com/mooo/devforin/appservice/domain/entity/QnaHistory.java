package com.mooo.devforin.appservice.domain.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@Entity
@NoArgsConstructor
@Table(name = "qna_history")
public class QnaHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "history_sn")
    private Integer historySn;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "chatSn", referencedColumnName = "chat_sn")
    private QnaChat qnaChat;

    @Column(name = "user_query")
    private String userQuery;

    @Column(name = "answer")
    private String answer;

    @Column(name = "session_id")
    private String sessionId;

    @Column(name = "request_id")
    private String requestId;

    @Column(name = "data_id")
    private String dataId;

    @Column(name = "feedback_sn")
    private Integer feedbackSn;

    @Column(name = "answer_revaluation")
    private String answerRevaluation;

    @Column(name = "created_by")
    private String createdBy;

    @Column(name = "created_at")
    private Timestamp createdAt;

    @Column(name = "updated_by")
    private String updatedBy;

    @Column(name = "updated_at")
    private Timestamp updatedAt;

    @Builder
    public QnaHistory(Integer historySn, QnaChat qnaChat, String userQuery, String answer, String sessionId, String requestId, String dataId, Integer feedbackSn, String answerRevaluation, String createdBy, Timestamp createdAt, String updatedBy, Timestamp updatedAt) {
        this.historySn = historySn;
        this.qnaChat = qnaChat;
        this.userQuery = userQuery;
        this.answer = answer;
        this.sessionId = sessionId;
        this.requestId = requestId;
        this.dataId = dataId;
        this.feedbackSn = feedbackSn;
        this.answerRevaluation = answerRevaluation;
        this.createdBy = createdBy;
        this.createdAt = createdAt;
        this.updatedBy = updatedBy;
        this.updatedAt = updatedAt;
    }
}
