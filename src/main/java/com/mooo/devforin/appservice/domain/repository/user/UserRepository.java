package com.mooo.devforin.appservice.domain.repository.user;

import com.mooo.devforin.appservice.domain.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, String>, UserRepositoryCustom {

}
