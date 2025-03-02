package com.mooo.devforin.appservice.service.question.impl;

import com.mooo.devforin.appservice.common.ApiResponseStatus;
import com.mooo.devforin.appservice.config.global.CustomUserDetails;
import com.mooo.devforin.appservice.controller.question.dto.QuestionRequestDto;
import com.mooo.devforin.appservice.domain.entity.Question;
import com.mooo.devforin.appservice.domain.repository.question.QuestionRepository;
import com.mooo.devforin.appservice.service.question.QuestionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class QuestionServiceImpl implements QuestionService {

    private final QuestionRepository questionRepository;

//    private static final String OPENAI_API_URL = "https://api.openai.com/v1/completions";
    private static final String DEEPSEEK_API_URL = "https://openrouter.ai/api/v1/chat/completions";

    @Value("${openAI.API.KEY}")
    private String API_KEY;

    @Override
    public Question createQuestion(QuestionRequestDto requestDto, UserDetails user) {

        if(ObjectUtils.isEmpty(requestDto.getQ())){
            throw new IllegalArgumentException("requestDto { q } cannot be null");
        }

        // User 정보 get
        CustomUserDetails customUserDetails = (CustomUserDetails) user;

        return questionRepository.save(Question.builder()
                .userId(customUserDetails.getId())
                .question(requestDto.getQ())
                .createdAt(Timestamp.valueOf(LocalDateTime.now()))
                .build());
    }

    @Override
    public String answerQuestion(Question question) {
        RestTemplate restTemplate = new RestTemplate();

        // 헤더 설정
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer " + API_KEY);

        log.info("requestDto.getQ() : {}", question.getQuestion());

        // 고정된 프롬프트 설정
        String systemPrompt = "너는 고양이 캐릭터인거야. 말의 끝마다 '냥' 이라고 붙여서 대답해~ 귀여운 고양이인거야, 이름은 '루'이고, 친구처럼 반말로 대답하는게 컨셉이야";

        // 요청 데이터 설정
        String requestBody = String.format(
                "{ \"model\": \"deepseek/deepseek-chat:free\", \"messages\": [" +
                        "{ \"role\": \"system\", \"content\": \"%s\" }, " +
                        "{ \"role\": \"user\", \"content\": \"%s\" }" +
                        "] }",
                systemPrompt, question.getQuestion()
        );

        HttpEntity<String> entity = new HttpEntity<>(requestBody, headers);

        // 최대 3번까지 시도
        int attempt = 0;
        int maxAttempts = 3;
        String content = "";

        while (attempt < maxAttempts) {
            // POST 요청 보내기
            ResponseEntity<String> response = restTemplate.exchange(
                    DEEPSEEK_API_URL,
                    HttpMethod.POST,
                    entity,
                    String.class
            );

            // 응답 상태 코드가 200이면 응답 본문을 처리
            if (response.getStatusCode() == HttpStatus.OK) {
                log.info("response : {} ", response);
                try {
                    // JSON 응답에서 'message.content' 값을 추출
                    JSONObject jsonResponse = new JSONObject(response.getBody());
                    if (jsonResponse.has("choices") && jsonResponse.getJSONArray("choices").length() > 0) {
                        content = jsonResponse
                                .getJSONArray("choices")
                                .getJSONObject(0)
                                .getJSONObject("message")
                                .getString("content");

                        // 답변이 있으면 종료
                        if (!content.isEmpty()) {

                            question.setAnswer(content);
                            question.setUpdatedAt(Timestamp.valueOf(LocalDateTime.now()));
                            questionRepository.save(question);

                            return content;  // 실제 답변
                        }
                    } else {
                        return "응답에서 'choices' 필드를 찾을 수 없습니다.";
                    }
                } catch (Exception e) {
                    return "JSON 파싱 중 오류 발생: " + e.getMessage();
                }
            } else {
                // 상태 코드가 200이 아닌 경우
                return "API에서 데이터를 가져오지 못했습니다. 상태 코드: " + response.getStatusCode();
            }

            attempt++;  // 시도 횟수 증가

            // 답변이 없으면 잠시 대기 후 재시도 (예: 2초 대기)
            try {
                Thread.sleep(2000);  // 2초 대기
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();  // 인터럽트 처리
            }
        }

        // 최대 시도 횟수까지 응답이 없으면 시스템 에러 및 고정 답변 출력
        question.setAnswer(ApiResponseStatus.ERROR_CASE_UNKNOWN.getMessage());
        question.setUpdatedAt(Timestamp.valueOf(LocalDateTime.now()));
        questionRepository.save(question);

        return "미안하다냥 ㅠㅠ 이따가 다시 찾아줘 !";
    }
}
