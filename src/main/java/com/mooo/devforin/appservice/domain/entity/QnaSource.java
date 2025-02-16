package com.mooo.devforin.appservice.domain.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.util.Objects;

@Data
@Entity
@NoArgsConstructor
@Table(name = "qna_source")
public class QnaSource {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "source_sn")
    private Integer sourceSn;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "historySn", referencedColumnName = "history_sn")
    private QnaHistory qnaHistory;

    @Column(name = "id")
    private String id;

    @Column(name = "doc_type")
    private String docType;

    @Column(name = "created_by")
    private String createdBy;

    @Column(name = "created_at")
    private Timestamp createdAt;

    @Column(name = "updated_by")
    private String updatedBy;

    @Column(name = "updated_at")
    private Timestamp updatedAt;

    public QnaSource(Integer sourceSn, QnaHistory qnaHistory, String id, String docType, String createdBy, Timestamp createdAt, String updatedBy, Timestamp updatedAt) {
        this.sourceSn = sourceSn;
        this.qnaHistory = qnaHistory;
        this.id = id;
        this.docType = docType;
        this.createdBy = createdBy;
        this.createdAt = createdAt;
        this.updatedBy = updatedBy;
        this.updatedAt = updatedAt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        QnaSource qnaSource = (QnaSource) o;
        return Objects.equals(id, qnaSource.id) &&
                Objects.equals(docType, qnaSource.docType);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, docType);
    }
}