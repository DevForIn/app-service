package com.mooo.devforin.appservice.controller.question;

import com.mooo.devforin.appservice.common.ApiResponseStatus;
import com.mooo.devforin.appservice.config.global.ResponseDTO;
import com.mooo.devforin.appservice.config.global.ResponseUtil;
import com.mooo.devforin.appservice.controller.question.dto.QuestionRequestDto;
import com.mooo.devforin.appservice.domain.entity.Question;
import com.mooo.devforin.appservice.service.question.QuestionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;


/**
 * 25/02/26 JeongIn's OpenAI - Chat Service Controller
 *
 */
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/question")
public class QuestionController {

    private final QuestionService questionService;

    /**
     *
     * @param requestDto
     * @param user                  @AuthenticationPrincipal 사용 방식 , 컨트롤러에서 인증된 사용자 정보를 바로 주입받음
     * @return
     */
    @PostMapping()
    public ResponseDTO askQuestion(@RequestBody QuestionRequestDto requestDto, @AuthenticationPrincipal UserDetails user){

        // 질의 테이블 생성
        Question question = questionService.createQuestion(requestDto,user);

        // 답변 생성
        String response = questionService.answerQuestion(question);

        return ResponseUtil.SUCCESS(ApiResponseStatus.SUCCESS.getCode(),ApiResponseStatus.SUCCESS.getMessage(), response);
    }



    @GetMapping()
    public void test(){
        // 로그인 한 사용자의 아이디를 불러오기 -> SecurityContextHolder 사용
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();
//        log.info("user info : {}",customUserDetails.toString());

        log.info("인증 성공");
    }
}
