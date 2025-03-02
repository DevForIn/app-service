package com.mooo.devforin.appservice.domain.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.sql.Timestamp;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "questions")
public class Question {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", updatable = false)
    private String userId;

    @Column(nullable = false)
    private String question;

    @Column(nullable = true)
    private String answer; // JSONB 타입

    @Column(name = "created_at", updatable = false)
    @CreationTimestamp
    private Timestamp createdAt;

    @Column(name = "updated_at", updatable = false)
    @CreationTimestamp
    private Timestamp updatedAt;

    @Builder
    public Question(String userId, String question, String answer, Timestamp createdAt, Timestamp updatedAt) {
        this.userId = userId;
        this.question = question;
        this.answer = answer;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
}
