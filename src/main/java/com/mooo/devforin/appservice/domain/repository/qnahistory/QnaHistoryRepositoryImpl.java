package com.mooo.devforin.appservice.domain.repository.qnahistory;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.mooo.devforin.appservice.domain.entity.QQnaHistory;
import com.mooo.devforin.appservice.domain.entity.QnaChat;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
public class QnaHistoryRepositoryImpl implements QnaHistoryRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;

    /**
     * 채팅 대화 내역 수 조회
     * @param qnaChat
     * @return
     */
    @Override
    public Integer getQnaHistoryListCount(QnaChat qnaChat) {
        QQnaHistory qQnaHistory = QQnaHistory.qnaHistory;

        return Optional.ofNullable(
                jpaQueryFactory.select(qQnaHistory.count())
                        .from(qQnaHistory)
                        .where(qQnaHistory.qnaChat.chatSn.eq(qnaChat.getChatSn()))
                        .fetchOne()
        ).orElse(0L).intValue();
    }
}
