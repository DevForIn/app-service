package com.mooo.devforin.appservice.domain.repository.qnafeedback;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class QnaFeedbackRepositoryImpl implements QnaFeedbackRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;

}
