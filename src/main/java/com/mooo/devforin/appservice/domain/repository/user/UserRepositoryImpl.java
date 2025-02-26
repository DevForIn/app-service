package com.mooo.devforin.appservice.domain.repository.user;

import com.mooo.devforin.appservice.domain.entity.User;
import com.mooo.devforin.appservice.domain.entity.QAdminUsers;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public void test() {
        QAdminUsers qAdminUsers = QAdminUsers.adminUsers;
        User user;
        user = jpaQueryFactory.select(qAdminUsers)
                .from(qAdminUsers)
                .where(qAdminUsers.id.eq("test"))
                .fetchOne();

        log.info(user.getId());
        log.info(user.getUsername());

    }
}
