package com.mooo.devforin.appservice.service.question.impl;

import com.mooo.devforin.appservice.controller.question.dto.QuestionRequestDto;
import com.mooo.devforin.appservice.service.question.QuestionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Service
@RequiredArgsConstructor
public class QuestionServiceImpl implements QuestionService {

//    private static final String OPENAI_API_URL = "https://api.openai.com/v1/completions";
    private static final String DEEPSEEK_API_URL = "https://openrouter.ai/api/v1/chat/completions";

    @Value("${openAI.API.KEY}")
    private String API_KEY;

    @Override
    public String askQuestion(QuestionRequestDto requestDto, UserDetails user) {
        RestTemplate restTemplate = new RestTemplate();

        // 헤더 설정
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer " + API_KEY);

        // 요청 데이터 설정
        String requestBody = String.format(
                "{\"model\": \"deepseek/deepseek-chat:free\", \"messages\": [{\"role\": \"user\", \"content\": \"%s\"}]}",
                requestDto.getQ());

        HttpEntity<String> entity = new HttpEntity<>(requestBody, headers);

        // POST 요청 보내기
        ResponseEntity<String> response = restTemplate.exchange(
                DEEPSEEK_API_URL,
                HttpMethod.POST,
                entity,
                String.class
        );

        // 응답 상태 코드가 200이면 응답 본문을 처리
        if (response.getStatusCode() == HttpStatus.OK) {
            log.info("response : {} ",response);
            try {
                // JSON 응답에서 'message.content' 값을 추출
                JSONObject jsonResponse = new JSONObject(response.getBody());
                if (jsonResponse.has("choices") && jsonResponse.getJSONArray("choices").length() > 0) {
                    String content = jsonResponse
                            .getJSONArray("choices")
                            .getJSONObject(0)
                            .getJSONObject("message")
                            .getString("content");

                    return content;  // 실제 답변
                } else {
                    // 'choices' 필드가 없을 경우 처리
                    return "응답에서 'choices' 필드를 찾을 수 없습니다.";
                }
            } catch (Exception e) {
                return "JSON 파싱 중 오류 발생: " + e.getMessage();
            }
        } else {
            return "API에서 데이터를 가져오지 못했습니다. 상태 코드: " + response.getStatusCode();
        }
    }
}
