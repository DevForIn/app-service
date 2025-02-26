package com.mooo.devforin.appservice.domain.repository.question;

import com.mooo.devforin.appservice.domain.entity.Question;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuestionRepository extends JpaRepository<Question,Long>, QuestionRepositoryCustom{

}
