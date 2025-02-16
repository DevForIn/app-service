package com.mooo.devforin.appservice.domain.repository.qnachat;

import com.mooo.devforin.appservice.domain.entity.QnaHistory;
import com.mooo.devforin.appservice.domain.entity.QnaSource;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.mooo.devforin.appservice.controller.qna.dto.QnaChatListDTO;
import com.mooo.devforin.appservice.controller.qna.dto.QnaChatListResponseDTO;
import com.mooo.devforin.appservice.domain.entity.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
public class QnaChatRepositoryImpl implements QnaChatRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;

    /**
     * Assistant 채팅 목록 조회
     *
     * @param id
     * @param pageNum
     * @return
     */
    @Override
    public QnaChatListResponseDTO getChatList(String id, int pageNum) {

        QQnaChat qQnaChat = QQnaChat.qnaChat;
        QQnaHistory qQnaHistory = QQnaHistory.qnaHistory;

        // 현재 날짜에서 pageNum을 이용해 기준 날짜 계산
        Timestamp startDate = Timestamp.valueOf(LocalDateTime.now().truncatedTo(ChronoUnit.DAYS) // 오늘의 00시
                .minusDays(30L * pageNum)); // 30일 * pageNum 이전

        // subquery: 존재하는지 여부 확인
        Boolean dataPlag = jpaQueryFactory.select(
                JPAExpressions.selectOne()
                        .from(qQnaChat)
                        .where(qQnaChat.chatId.eq(id)
                                .and(qQnaChat.createdAt.lt(startDate))
                        )
                        .exists()
        ).from(qQnaChat)
        .fetchOne();

        // Assistant 채팅 목록 조회 메인 쿼리
        List<QnaChatListDTO> dtoList =  jpaQueryFactory.select(
                        Projections.constructor(QnaChatListDTO.class,
                                qQnaChat.chatSn,
                                qQnaHistory.historySn,
                                qQnaHistory.userQuery,
                                qQnaChat.createdAt,
                                qQnaChat.updatedAt)
                )
                .from(qQnaChat)
                .leftJoin(qQnaHistory).on(qQnaChat.chatSn.eq(qQnaHistory.qnaChat.chatSn)
                        .and(qQnaHistory.historySn.eq(
                                JPAExpressions.select(qQnaHistory.historySn.min())
                                        .from(qQnaHistory)
                                        .where(qQnaHistory.qnaChat.chatSn.eq(qQnaChat.chatSn))
                        )))
                .where(
                        qQnaChat.chatId.eq(id)
                                .and(qQnaChat.createdAt.goe(startDate) // 계산된 날짜 기준으로 데이터 조회
                                .and(qQnaChat.useYn.eq("Y"))))  // 활성화 리스트 조회
                .orderBy(qQnaChat.createdAt.desc())
                .fetch();

        // 결과 반환
        return new QnaChatListResponseDTO(dataPlag, dtoList);
    }

    /**
     * 질문 & 답변 목록 조회
     *
     * @param chatSn
     * @return
     */
    @Override
    public List<QnaHistory> selectChatHistoryList(int chatSn) {
        QQnaHistory qQnaHistory = QQnaHistory.qnaHistory;

        return jpaQueryFactory.select(qQnaHistory)
                        .from(qQnaHistory)
                        .where(qQnaHistory.qnaChat.chatSn.eq(chatSn))
                        .orderBy(qQnaHistory.createdAt.asc())
                        .fetch();
    }

    /**
     * chunk 목록 조회
     *
     * @param historySnList
     * @return
     */
    @Override
    public List<QnaSource> selectChatSourceList(List<Integer> historySnList) {
        QQnaSource qQnaSource = QQnaSource.qnaSource;
        return  jpaQueryFactory.select(qQnaSource)
                        .from(qQnaSource)
                        .where(qQnaSource.qnaHistory.historySn.in(historySnList))
                        .fetch();

    }
}
