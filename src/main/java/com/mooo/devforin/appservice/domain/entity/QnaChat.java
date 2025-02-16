package com.mooo.devforin.appservice.domain.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;


@Data
@Entity
@NoArgsConstructor
@Table(name = "qna_chat")
public class QnaChat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "chat_sn")
    private Integer chatSn;

    @Column(name = "chat_id")
    private String chatId;

    @Column(name = "use_yn")
    private String useYn;

    @Column(name = "created_by")
    private String createdBy;

    @Column(name = "created_at")
    private Timestamp createdAt;

    @Column(name = "updated_by")
    private String updatedBy;

    @Column(name = "updated_at")
    private Timestamp updatedAt;

    @Builder
    public QnaChat(Integer chatSn, String chatId, String useYn, String createdBy, Timestamp createdAt, String updatedBy, Timestamp updatedAt) {
        this.chatSn = chatSn;
        this.chatId = chatId;
        this.useYn = useYn;
        this.createdBy = createdBy;
        this.createdAt = createdAt;
        this.updatedBy = updatedBy;
        this.updatedAt = updatedAt;
    }
}
