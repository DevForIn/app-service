package com.mooo.devforin.appservice.service.qnasource;

import com.mooo.devforin.appservice.controller.qna.dto.QnaAnswerResponseDTO;
import com.mooo.devforin.appservice.controller.qna.dto.QnaSourceResponseDTO;
import com.mooo.devforin.appservice.domain.entity.QnaChat;
import com.mooo.devforin.appservice.domain.entity.QnaHistory;
import com.mooo.devforin.appservice.domain.entity.QnaSource;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface QnaSourceService {

    /**
     * 사용자 문의 - 출처(chunk data) 데이터 생성
     *
     * @param qnaHistory
     * @param dto
     * @return
     */
    List<QnaSourceResponseDTO> createQnaSource(QnaHistory qnaHistory, ResponseEntity<QnaAnswerResponseDTO> dto);

    /**
     * 사용자 문의 - 출처 리스트 조회
     * @param qnaChat
     * @return
     */
    List<QnaSource> requestQnaSource(QnaChat qnaChat);
}

