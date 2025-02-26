package com.mooo.devforin.appservice.service.question;

import com.mooo.devforin.appservice.controller.question.dto.QuestionRequestDto;
import org.springframework.security.core.userdetails.UserDetails;

public interface QuestionService {
    String askQuestion(QuestionRequestDto requestDto, UserDetails user);
}
