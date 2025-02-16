package com.mooo.devforin.appservice.domain.repository.qnasource;

import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.transaction.Transactional;
import com.mooo.devforin.appservice.domain.entity.QQnaHistory;
import com.mooo.devforin.appservice.domain.entity.QQnaSource;
import com.mooo.devforin.appservice.domain.entity.QnaChat;
import com.mooo.devforin.appservice.domain.entity.QnaSource;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
public class QnaSourceRepositoryImpl implements QnaSourceRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    @Transactional
    public List<QnaSource> requestQnaSource(QnaChat qnaChat) {

        QQnaSource qQnaSource = QQnaSource.qnaSource;
        QQnaHistory qQnaHistory = QQnaHistory.qnaHistory;

        return jpaQueryFactory.select(qQnaSource)
                .from(qQnaSource)
                .where(qQnaHistory.historySn.eq(
                        JPAExpressions.select(qQnaHistory.historySn.min())
                                .from(qQnaHistory)
                                .where(qQnaHistory.qnaChat.eq(qnaChat))
                ))
                .fetch();
    }
}
