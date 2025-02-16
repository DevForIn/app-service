package com.mooo.devforin.appservice.domain.repository.qnahistory;

import com.mooo.devforin.appservice.domain.entity.QnaHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QnaHistoryRepository extends JpaRepository<QnaHistory, Integer>, QnaHistoryRepositoryCustom {

}
