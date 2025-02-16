package com.mooo.devforin.appservice.controller.qna;

import com.mooo.devforin.appservice.config.global.CustomUserDetails;
import com.mooo.devforin.appservice.config.global.ResponseDTO;
import com.mooo.devforin.appservice.config.global.ResponseUtil;
import com.mooo.devforin.appservice.controller.qna.dto.*;
import com.mooo.devforin.appservice.domain.entity.QnaChat;
import com.mooo.devforin.appservice.domain.entity.QnaFeedback;
import com.mooo.devforin.appservice.domain.entity.QnaHistory;
import com.mooo.devforin.appservice.domain.entity.QnaSource;
import com.mooo.devforin.appservice.service.qnachat.QnaChatService;
import com.mooo.devforin.appservice.service.qnafeedback.QnaFeedbackService;
import com.mooo.devforin.appservice.service.qnahistory.QnaHistoryService;
import com.mooo.devforin.appservice.service.qnasource.QnaSourceService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Q & A 연동 Controller
 *
 * @author 정정인
 * @version 1.0.0
 *
 */
@Slf4j  
@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/qna")
public class QnaController {

    private final QnaChatService qnaChatService;
    private final QnaHistoryService qnaHistoryService;
    private final QnaSourceService qnaSourceService;
    private final QnaFeedbackService qnaFeedbackService;

    /**
     * 심사Q&A 문의 or 멀티턴 요청 API
     *
     * @param   dto Q&A 질의 시작용 Dto
     * @return
     */
    @PostMapping("/request-inquiry")
    public ResponseDTO requestInquiry(@RequestBody QnaRequestDTO dto) {
        // TODO - 테스트 후 제거 24/12/06
        // 시작 시간 기록
        long startTime = System.currentTimeMillis();

        if(ObjectUtils.isEmpty(dto)){
            return ResponseUtil.ERROR(400, "Request Dto is Null or Empty", null);
        }
        if(ObjectUtils.isEmpty(dto.getInput())){
            return ResponseUtil.ERROR(400, "Request Dto.getInput is Null or Empty ", null);
        }
        if(ObjectUtils.isEmpty(dto.getInput())){
            return ResponseUtil.ERROR(400, "Request Dto.getInput.get(0) is Null or Empty", null);
        }

        if((dto.getChatSn() == null && "false".equals(dto.getInput().get(0).isAnswer))
                || dto.getChatSn() != null && "true".equals(dto.getInput().get(0).isAnswer)){
            return ResponseUtil.ERROR(404, "잘못된 요청입니다. 요청 정보를 확인해주세요.", null);
        }

        // 로그인 한 사용자의 아이디를 불러오기 위함
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();

        QnaChat qnaChat = null;

        // chatSn 값으로 QnaChat 조회
        if (dto.getChatSn() != null) {
            qnaChat = qnaChatService.selectQnaChat(dto.getChatSn());
            if(qnaChat == null || qnaHistoryService.getQnaHistoryCount(qnaChat) > 19) {
                return ResponseUtil.ERROR(404, "요청 데이터를 확인해주세요.", null);
            }
        }

        if (dto.getChatSn() == null) {
            qnaChat = qnaChatService.createQnaChat(customUserDetails.getId());
        }

        // 질문이력 생성
        QnaHistory qnaHistory = qnaHistoryService.createQnaHistory(qnaChat, dto.getInput().get(0).user_query);

        if("true".equals(dto.getInput().get(0).isAnswer)) {

            // Plugnet MultiIntent - 멀티의도 분류
            ResponseEntity<QnaMultiIntentResponseDTO> resMultiIntent = qnaHistoryService.requestPlugNetMultiIntent(dto);

            log.info("멀티턴 SessionId : {} ",resMultiIntent.getBody().getSessionId());

            if (isResponseFailure(resMultiIntent,
                    r -> r.getResult().getData(),      // 데이터 리스트 추출
                    QnaMultiIntentResponseDTO.Data::get__status__)) {           // __status__ 값 추출

                String errorMessage = extractErrorMessages(resMultiIntent.getBody().getResult().getData());

                return ResponseUtil.ERROR(999, "Plugnet Multi Turn - 벡터 검색 Data Result Status : FAILURE", errorMessage);
            }

            if (!"200".equals(Objects.requireNonNull(resMultiIntent.getBody()).getCode())) {
                return ResponseUtil.ERROR(resMultiIntent.getStatusCode().value(), "plugnet multi-intent 연동 확인 필요", null);
            }

            // PlugNet Process 처리
            ResponseEntity<QnaAnswerResponseDTO> resProcess = qnaHistoryService.requestPlugNetProcess(resMultiIntent.getBody());

            log.info("프로세스 SessionId : {} ",resProcess.getBody().getSessionId());

            if (isResponseFailure(resProcess,
                    r -> r.getResult().getData(),      // 데이터 리스트 추출
                    QnaAnswerResponseDTO.Data::get__status__)) {           // __status__ 값 추출

                String errorMessage = extractErrorMessages(resProcess.getBody().getResult().getData());

                return ResponseUtil.ERROR(999, "Plugnet Process Data Result Status : FAILURE", errorMessage);
            }
            if (!"200".equals(Objects.requireNonNull(resProcess.getBody()).getCode())) {
                return ResponseUtil.ERROR(resProcess.getStatusCode().value(), "plugnet Process 연동 확인 필요", null);
            }

            List<QnaSourceResponseDTO> sourceList = qnaSourceService.createQnaSource(qnaHistory, resProcess);

            // PlugNet Final Answer 처리
            ResponseEntity<QnaFinalAnswerResponseDTO> resFinalAnswer = qnaHistoryService.requestPlugNetFinalAnswer(dto, resProcess.getBody());

            log.info("Final Answer SessionId : {} ",resFinalAnswer.getBody().getSessionId());

            if (isResponseFailure(resFinalAnswer,
                    r -> r.getResult().getData(),      // 데이터 리스트 추출
                    QnaFinalAnswerResponseDTO.Data::get__status__)) {           // __status__ 값 추출

                String errorMessage = extractErrorMessages(resFinalAnswer.getBody().getResult().getData());

                return ResponseUtil.ERROR(999, "Plugnet Final Answer Data Result Status : FAILURE", errorMessage);
            }

            if (!"200".equals(Objects.requireNonNull(resFinalAnswer.getBody()).getCode())) {
                return ResponseUtil.ERROR(resFinalAnswer.getStatusCode().value(), "plugnet Final Answer 연동 확인 필요", null);
            }

            // 최종 답변 업데이트
            qnaChatService.updateQnaChat(customUserDetails.getId(), qnaChat);
            qnaHistoryService.updateFinalAnswer(qnaHistory, resFinalAnswer.getBody());

            // 최종 Response Set up
            QnaResponseDTO responseDTO = qnaHistoryService.modifyResults(qnaChat, resFinalAnswer.getBody(), qnaHistory, sourceList);

            // TODO - 테스트 후 제거 24/12/06
            // 실행 시간 측정
            long endTime = System.currentTimeMillis();
            double executionTIme = (endTime - startTime)/1000.0;
            log.info("requestInquiry API 실행 시간: {} s",  executionTIme);

            // TODO - 테스트 후 제거 24/12/06
            return ResponseUtil.SUCCESS(200, "심사 Q&A 최종 답변 성공. 답변 실행 시간 측정 : " + executionTIme + "초", responseDTO);
//            return ResponseUtil.SUCCESS(200, "심사 Q&A 최종 답변 성공", responseDTO);
        }else if("false".equals(dto.getInput().get(0).isAnswer)) {

            if(ObjectUtils.isEmpty(dto.getSessionId()) || ObjectUtils.isEmpty(dto.getChatSn())){
                return ResponseUtil.ERROR(404, "요청 데이터를 확인해주세요.", null);
            }

            // chunk Data 추출
            List<QnaSource> qnaSourceList = qnaSourceService.requestQnaSource(qnaChat);

            // Plugnet Multi Turn - 대화 처리
            ResponseEntity<QnaFinalAnswerResponseDTO> response = qnaHistoryService.requestPlugNetMultiTurnAnswer(dto,qnaSourceList,qnaChat);

            if (isResponseFailure(response,
                    r -> r.getResult().getData(),      // 데이터 리스트 추출
                    QnaFinalAnswerResponseDTO.Data::get__status__)) {           // __status__ 값 추출

                String errorMessage = extractErrorMessages(response.getBody().getResult().getData());

                return ResponseUtil.ERROR(999, "Plugnet Multi Turn - 대화 처리 Data Result Status : FAILURE", errorMessage);
            }

            if(!"200".equals(Objects.requireNonNull(response.getBody()).getCode())){
                return ResponseUtil.ERROR(response.getStatusCode().value(),"plugnet Multi Turn 연동 확인 필요",null);
            }
            // 수정 이력 업데이트
            qnaChatService.updateQnaChat(customUserDetails.getId(), qnaChat);
            qnaHistoryService.updateMultiTurnAnswer(qnaHistory, response.getBody());

            // 최종 Response Set up
            QnaResponseDTO responseDTO = qnaHistoryService.modifyResults(qnaChat, response.getBody(), qnaHistory, null);

            // 채팅 내역 20개이상 시 false.
            Integer chatHistoryCount = qnaHistoryService.getQnaHistoryCount(qnaChat);

            if(chatHistoryCount > 19){
                responseDTO.setMultiTurnStatus(false);
            }

            return ResponseUtil.SUCCESS(200,"Multi-Turn 답변 성공",responseDTO);

        }
        return ResponseUtil.ERROR(500,"plugnet 연동 확인 필요",null);
    }


    /**
     * 심사 Q&A chat Assistant List 조회 API
     *
     * @param pageNum
     * @return
     */
    @GetMapping("/chat-list")
    public ResponseDTO chatList(@RequestParam(required = false, defaultValue = "1") int pageNum){

        // pageNum 1 이상이어야 함
        if (pageNum < 1) {
            return ResponseUtil.ERROR(400, "pageNum 값은 1 이상이어야 합니다.", null);
        }

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();

        QnaChatListResponseDTO chatList = qnaChatService.getChatList(customUserDetails.getId(), pageNum);

        return ResponseUtil.SUCCESS(200, "Get Chat List Success", chatList);
    }

    /**
     * 심사 Q&A chat Assistant 대화 이력 상세 조회 API
     *
     * @param chatSn
     * @return
     */
    @GetMapping("/view-chat-history")
    public ResponseDTO ViewChatHistory(@RequestParam(required = false) Integer chatSn){

        if (chatSn == null || chatSn < 1) {
            return ResponseUtil.ERROR(400, "ChatSn가 존재하지않거나 1보다 작습니다.", null);
        }

        QnaChat qnaChat = qnaChatService.selectQnaChat(chatSn);

        if (ObjectUtils.isEmpty(qnaChat)) {
            return ResponseUtil.ERROR(404, "요청하신 채팅 내역을 찾을 수 없습니다. 입력값을 확인해 주세요.", null);
        }

        // 채팅 내역 20개이상 시 false.
        Integer chatHistoryCount = qnaHistoryService.getQnaHistoryCount(qnaChat);

        ChatHistoryViewResponseDTO chatHistory = qnaChatService.viewChatHistory(chatSn, chatHistoryCount);

        if (ObjectUtils.isEmpty(chatHistory)) {
            return ResponseUtil.ERROR(404, "chat History 내역이 없습니다.", null);
        }
        return ResponseUtil.SUCCESS(200, "View Chat History Success", chatHistory);
    }

    /**
     * 심사 Q&A chat Assistant 대화 이력 삭제 처리
     *  use_yn : 'Y' -> 'N"
     *
     * @param dto
     * @return
     */
    @PostMapping("/delete-chat-history")
    public ResponseDTO deleteChatHistory(@RequestBody DeleteChatDTO dto){

        if (dto.getChatSn() == null || dto.getChatSn()  < 1) {
            return ResponseUtil.ERROR(400, "해당 채팅이 존재하지 않거나 요청 값을 상이합니다.", null);
        }

        // 로그인 한 사용자의 아이디를 불러오기 위함
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();

        QnaChat qnaChat = qnaChatService.selectQnaChat(dto.getChatSn());

        if (ObjectUtils.isEmpty(qnaChat)) {
            return ResponseUtil.ERROR(404, "요청하신 채팅 내역을 찾을 수 없습니다. 입력값을 확인해 주세요.", null);
        }

        qnaChatService.deleteChatHistory(qnaChat,customUserDetails.getId());

        return ResponseUtil.SUCCESS(200, "Delete Chat History Success", null);
    }

    /**
     * 심사 Q&A chat Assistant 답변 평가
     *
     * @param dto
     * @return
     */
    @PostMapping("/update-answer-revaluation")
    public ResponseDTO updateAnswerRevaluation(@RequestBody AnswerRevaluationDTO dto){

        if (dto.getHistorySn() == null || dto.getHistorySn()  < 1) {
            return ResponseUtil.ERROR(400, "해당 질의 응답이 존재하지 않거나 요청 값을 상이합니다.", null);
        }

        if(!"GOOD".equals(dto.getRevaluation()) && !"BAD".equals(dto.getRevaluation())){
            return ResponseUtil.ERROR(400, "요청 정보를 확인하세요.", null);
        }

        // 로그인 한 사용자의 아이디를 불러오기 위함
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();

        QnaHistory qnaHistory = qnaHistoryService.selectQnaHistory(dto.getHistorySn());

        if (ObjectUtils.isEmpty(qnaHistory)) {
            return ResponseUtil.ERROR(404, "요청하신 채팅을 찾을 수 없습니다. 입력값을 확인해 주세요.", null);
        }

        if (qnaHistory.getAnswerRevaluation() != null) {
            return ResponseUtil.ERROR(400, "이미 답변 평가가 존재합니다. 데이터를 확인하세요.", null);
        }

        qnaHistoryService.updateQnaHistoryAnswerRevaluation(dto, qnaHistory,customUserDetails.getId());


        return ResponseUtil.SUCCESS(200, "Update Answer Revaluation Success", null);
    }

    /**
     * 심사 Q&A chat Assistant 피드백 생성
     *
     * @param dto
     * @return
     */
    @PostMapping("/create-feedback")
    public ResponseDTO createFeedback(@RequestBody FeedbackDTO dto){

        if (dto.getHistorySn() == null || dto.getHistorySn()  < 1) {
            return ResponseUtil.ERROR(400, "해당 질의 응답이 존재하지 않거나 요청 값을 상이합니다.", null);
        }

        if(ObjectUtils.isEmpty(dto.getReferenceInfo())){
            return ResponseUtil.ERROR(400, "요청 정보를 확인하세요.", null);
        }

        // 로그인 한 사용자의 아이디를 불러오기 위함
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();

        QnaHistory qnaHistory = qnaHistoryService.selectQnaHistory(dto.getHistorySn());

        if (ObjectUtils.isEmpty(qnaHistory)) {
            return ResponseUtil.ERROR(404, "요청하신 채팅을 찾을 수 없습니다. 입력값을 확인해 주세요.", null);
        }

        if (qnaHistory.getFeedbackSn() != null) {
            return ResponseUtil.ERROR(400, "이미 Feedback이 존재합니다. 데이터를 확인하세요.", null);
        }

        QnaFeedback qnaFeedback = qnaFeedbackService.createFeedback(qnaHistory,dto,customUserDetails.getId());

        qnaHistoryService.updateQnaHistoryFeedback(qnaFeedback, qnaHistory,customUserDetails.getId());

        return ResponseUtil.SUCCESS(200, "Update Feedback Success", null);
    }

    /**
     * 심사 Q&A chat history - Feedback 상세 조회 API
     *
     * @param historySn
     * @return
     */
    @GetMapping("/view-feedback")
    public ResponseDTO ViewFeedback(@RequestParam(required = false) Integer historySn){
        log.info("1");
        if (historySn == null || historySn < 1) {
            return ResponseUtil.ERROR(400, "요청 데이터가 존재하지않거나 1보다 작습니다.", null);
        }

        QnaHistory qnaHistory = qnaHistoryService.selectQnaHistory(historySn);

        if (ObjectUtils.isEmpty(qnaHistory)) {
            return ResponseUtil.ERROR(404, "요청하신 채팅을 찾을 수 없습니다. 입력값을 확인해 주세요.", null);
        }

        if (ObjectUtils.isEmpty(qnaHistory.getFeedbackSn())) {
            return ResponseUtil.ERROR(404, "Feedback 찾을 수 없습니다. 입력값을 확인해 주세요.", null);
        }

        FeedbackViewResponseDTO feedbackViewResponseDTO = qnaFeedbackService.ViewFeedback(qnaHistory.getFeedbackSn());

        if (ObjectUtils.isEmpty(feedbackViewResponseDTO)) {
            return ResponseUtil.ERROR(404, "Feedback 내역이 없습니다.", null);
        }
        return ResponseUtil.SUCCESS(200, "View Feedback Success", feedbackViewResponseDTO);
    }













    /**
     * Plugnet Response Data List에 Status 값 체크
     */
    private <T, D> boolean isResponseFailure(ResponseEntity<T> response, Function<T, List<D>> dataExtractor, Function<D, String> statusExtractor) {
        try {
            // Response에서 데이터 리스트 추출
            List<D> dataList = dataExtractor.apply(response.getBody());
            for (D data : dataList) {
                // 데이터의 __status__ 값 추출 및 FAILURE 여부 확인
                if ("FAILURE".equals(statusExtractor.apply(data))) {
                    log.info("Plugnet Result Data Status is FAILURE ! X_X");
                    return true; // 실패 상태 발견 시 true 반환
                }
            }
        } catch (Exception e) {
            log.info("Plugnet Result Data is null ! ERROR Message : {} ", e.getMessage());
        }
        return false; // 모든 데이터가 성공 상태라면 false 반환
    }

    /**
     * Plugnet Result data - 에러 메시지를 추출하여 하나의 문자열로 반환
     */
    public static <T> String extractErrorMessages(List<T> result) {
        if (result == null || result.isEmpty()) {
            return "Result data is empty or null";
        }

        // 각 Data 객체에서 __error_message__를 추출하여 줄바꿈으로 연결
        return result.stream()
                .map(data -> {
                    if (data instanceof QnaFinalAnswerResponseDTO.Data) {
                        return ((QnaFinalAnswerResponseDTO.Data) data).get__error_message__();
                    } else if (data instanceof QnaAnswerResponseDTO.Data) {
                        return ((QnaAnswerResponseDTO.Data) data).get__error_message__();
                    } else if(data instanceof QnaMultiIntentResponseDTO.Data){
                        return ((QnaMultiIntentResponseDTO.Data) data).get__error_message__();
                    }else{
                        return "Unknown error message"; // 다른 데이터 타입에 대해 예외 처리
                    }
                })
                .collect(Collectors.joining("\n"));
    }
}