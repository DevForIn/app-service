package com.mooo.devforin.appservice.domain.repository.user;

import com.mooo.devforin.appservice.domain.entity.QUser;
import com.mooo.devforin.appservice.domain.entity.User;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public void test() {
        QUser qUser = QUser.user;
        User user;
        user = jpaQueryFactory.select(qUser)
                .from(qUser)
                .where(qUser.id.eq("test"))
                .fetchOne();

        log.info(user.getId());
        log.info(user.getUsername());

    }
}
