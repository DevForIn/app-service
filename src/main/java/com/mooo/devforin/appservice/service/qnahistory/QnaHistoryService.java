package com.mooo.devforin.appservice.service.qnahistory;

import com.mooo.devforin.appservice.controller.qna.dto.*;
import com.mooo.devforin.appservice.controller.qna.dto.*;
import com.mooo.devforin.appservice.domain.entity.QnaChat;
import com.mooo.devforin.appservice.domain.entity.QnaFeedback;
import com.mooo.devforin.appservice.domain.entity.QnaHistory;
import com.mooo.devforin.appservice.domain.entity.QnaSource;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface QnaHistoryService {

    /**
     * Origin 질의에 대해 Multi-Intent 분류 workflow 호출
     * @param   dto
     * @return
     */
    ResponseEntity<QnaMultiIntentResponseDTO> requestPlugNetMultiIntent(QnaRequestDTO dto);

    /**
     * Mulit-Intent 분류된 질의 List 값으로 Process workflow 호출
     * @param   dto
     * @return
     */
    ResponseEntity<QnaAnswerResponseDTO> requestPlugNetProcess(QnaMultiIntentResponseDTO dto);

    /**
     * FinalAnswer workflow 호출 - 최종 답변
     * @param   dto
     * @return
     */
    ResponseEntity<QnaFinalAnswerResponseDTO> requestPlugNetFinalAnswer(QnaRequestDTO originDto, QnaAnswerResponseDTO dto);

    /**
     * FinalAnswer workflow 호출 - 멀티턴 답변
     *
     * @param dto
     * @param qnaSourceList
     * @param qnaChat
     * @return
     */
    ResponseEntity<QnaFinalAnswerResponseDTO> requestPlugNetMultiTurnAnswer(QnaRequestDTO dto, List<QnaSource> qnaSourceList, QnaChat qnaChat);

    /**
     * 사용자 문의 최초 데이터 생성
     *
     * @param qnaChat
     * @param userQuery
     * @return
     */
    QnaHistory createQnaHistory(QnaChat qnaChat, String userQuery);

    /**
     * 사용자 문의 최종답변 데이터 업데이트
     *
     * @param qnaHistory
     * @param dto
     * @return
     */
    void updateFinalAnswer(QnaHistory qnaHistory, QnaFinalAnswerResponseDTO dto);

    /**
     * 사용자 문의 멀티턴 데이터 업데이트
     *
     * @param qnaHistory
     * @param dto
     * @return
     */
    void updateMultiTurnAnswer(QnaHistory qnaHistory, QnaFinalAnswerResponseDTO dto);

    /**
     * 최종 Response Data 수정
     *
     * @param qnaChat
     * @param dto
     * @param qnaHistory
     * @param sourceList
     * @return
     */
    QnaResponseDTO modifyResults(QnaChat qnaChat, QnaFinalAnswerResponseDTO dto, QnaHistory qnaHistory, List<QnaSourceResponseDTO> sourceList);

    /**
     * 채팅 대화 내역 수 조회
     * @param qnaChat
     * @return
     */
    Integer getQnaHistoryCount(QnaChat qnaChat);

    /**
     * historySn에 해당하는 질의응답 데이터 조회
     *
     * @param historySn
     * @return
     */
    QnaHistory selectQnaHistory(Integer historySn);

    /**
     * 해당 질의응답 데이터의 질문평가 업데이트
     *
     * @param dto
     * @param qnaHistory
     * @param id
     */
    void updateQnaHistoryAnswerRevaluation(AnswerRevaluationDTO dto, QnaHistory qnaHistory, String id);

    /**
     * 해당 질의응답 Feedback Sn update
     *
     * @param qnaFeedback
     * @param qnaHistory
     * @param id
     */
    void updateQnaHistoryFeedback(QnaFeedback qnaFeedback, QnaHistory qnaHistory, String id);

}


