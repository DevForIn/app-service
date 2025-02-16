package com.mooo.devforin.appservice.domain.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;


@Data
@Entity
@NoArgsConstructor
@Table(name = "qna_feedback")
public class QnaFeedback {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "feedback_sn")
    private Integer feedbackSn;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "historySn", referencedColumnName = "history_sn")
    private QnaHistory qnaHistory;

    @Column(name = "feedback_info")
    private String feedbackInfo;

    @Column(name = "reference_info")
    private String referenceInfo;

    @Column(name = "created_by")
    private String createdBy;

    @Column(name = "created_at")
    private Timestamp createdAt;

    @Column(name = "updated_by")
    private String updatedBy;

    @Column(name = "updated_at")
    private Timestamp updatedAt;

    @Builder
    public QnaFeedback(Integer feedbackSn, QnaHistory qnaHistory, String feedbackInfo, String referenceInfo, String createdBy, Timestamp createdAt, String updatedBy, Timestamp updatedAt) {
        this.feedbackSn = feedbackSn;
        this.qnaHistory = qnaHistory;
        this.feedbackInfo = feedbackInfo;
        this.referenceInfo = referenceInfo;
        this.createdBy = createdBy;
        this.createdAt = createdAt;
        this.updatedBy = updatedBy;
        this.updatedAt = updatedAt;
    }
}
