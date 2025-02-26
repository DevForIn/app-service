package com.mooo.devforin.appservice.service.question.impl;

import com.mooo.devforin.appservice.controller.question.dto.QuestionRequestDto;
import com.mooo.devforin.appservice.service.question.QuestionService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Service
@RequiredArgsConstructor
public class QuestionServiceImpl implements QuestionService {

    private static final String OPENAI_API_URL = "https://api.openai.com/v1/completions";

    @Value("${openAI.API.KEY}")
    private String API_KEY;

    private final RestTemplate restTemplate;

    @Override
    public String askQuestion(QuestionRequestDto requestDto, UserDetails user) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer " + API_KEY);

        // 요청 바디 설정 (gpt-3.5-turbo 모델로 변경)
        String requestBody = String.format("{\"model\": \"gpt-4o-mini\", \"messages\": [{\"role\": \"user\", \"content\": \"%s\"}], \"max_tokens\": 150}", requestDto.getQ());

        HttpEntity<String> entity = new HttpEntity<>(requestBody, headers);

        ResponseEntity<String> response = restTemplate.exchange(
                OPENAI_API_URL,
                HttpMethod.POST,
                entity,
                String.class
        );

        // 응답에서 필요한 데이터만 반환
        String responseBody = response.getBody();

        return responseBody;
    }
}
