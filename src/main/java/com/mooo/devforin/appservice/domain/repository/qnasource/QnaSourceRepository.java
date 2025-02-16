package com.mooo.devforin.appservice.domain.repository.qnasource;

import com.mooo.devforin.appservice.domain.entity.QnaSource;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QnaSourceRepository extends JpaRepository<QnaSource, Integer>, QnaSourceRepositoryCustom {

}
