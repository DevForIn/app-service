package com.mooo.devforin.appservice.domain.repository.qnafeedback;

import com.mooo.devforin.appservice.domain.entity.QnaFeedback;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QnaFeedbackRepository extends JpaRepository<QnaFeedback, Integer>, QnaFeedbackRepositoryCustom {

}
