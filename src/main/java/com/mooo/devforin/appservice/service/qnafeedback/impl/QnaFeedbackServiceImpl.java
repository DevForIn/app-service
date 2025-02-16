package com.mooo.devforin.appservice.service.qnafeedback.impl;

import jakarta.transaction.Transactional;
import com.mooo.devforin.appservice.controller.qna.dto.FeedbackDTO;
import com.mooo.devforin.appservice.controller.qna.dto.FeedbackViewResponseDTO;
import com.mooo.devforin.appservice.domain.entity.QnaFeedback;
import com.mooo.devforin.appservice.domain.entity.QnaHistory;
import com.mooo.devforin.appservice.domain.repository.qnafeedback.QnaFeedbackRepository;
import com.mooo.devforin.appservice.service.qnafeedback.QnaFeedbackService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class QnaFeedbackServiceImpl implements QnaFeedbackService {

    private final QnaFeedbackRepository qnaFeedbackRepository;

    @Override
    @Transactional
    public QnaFeedback createFeedback(QnaHistory qnaHistory, FeedbackDTO dto, String id) {
        return qnaFeedbackRepository.save(QnaFeedback.builder()
                .qnaHistory(qnaHistory)
                .feedbackInfo(dto.getFeedbackInfo())
                .referenceInfo(dto.getReferenceInfo())
                .createdBy(id)
                .createdAt(Timestamp.valueOf(LocalDateTime.now()))
                .build());
    }

    @Override
    @Transactional
    public FeedbackViewResponseDTO ViewFeedback(Integer feedbackSn) {

        QnaFeedback qnaFeedback = qnaFeedbackRepository.findById(feedbackSn).orElse(null);

        if(ObjectUtils.isEmpty(qnaFeedback)){
            return null;
        }

        return FeedbackViewResponseDTO.builder()
                .historySn(qnaFeedback.getQnaHistory().getHistorySn())
                .feedbackSn(qnaFeedback.getFeedbackSn())
                .feedbackInfo(qnaFeedback.getFeedbackInfo())
                .referenceInfo(qnaFeedback.getReferenceInfo())
                .createdBy(qnaFeedback.getCreatedBy())
                .createdAt(qnaFeedback.getCreatedAt())
                .build();
    }
}
