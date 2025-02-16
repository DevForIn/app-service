package com.mooo.devforin.appservice.domain.repository.user;

import com.mooo.devforin.appservice.domain.entity.AdminUsers;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<AdminUsers, String>, UserRepositoryCustom {

}
