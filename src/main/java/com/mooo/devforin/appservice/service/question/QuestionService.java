package com.mooo.devforin.appservice.service.question;

import com.mooo.devforin.appservice.controller.question.dto.QuestionRequestDto;
import com.mooo.devforin.appservice.domain.entity.Question;
import org.springframework.security.core.userdetails.UserDetails;

public interface QuestionService {

    Question createQuestion(QuestionRequestDto requestDto, UserDetails user);

    String answerQuestion(Question question);


}
