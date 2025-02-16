package com.mooo.devforin.appservice.domain.repository.user;

import com.mooo.devforin.appservice.domain.entity.AdminUsers;
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
        AdminUsers adminUsers;
        adminUsers = jpaQueryFactory.select(qAdminUsers)
                .from(qAdminUsers)
                .where(qAdminUsers.id.eq("cognet"))
                .fetchOne();

        log.info(adminUsers.getId());
        log.info(adminUsers.getUsername());

    }
}
