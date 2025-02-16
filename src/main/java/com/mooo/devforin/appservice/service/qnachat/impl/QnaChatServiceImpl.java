package com.mooo.devforin.appservice.service.qnachat.impl;

import com.mooo.devforin.appservice.controller.qna.dto.*;
import jakarta.transaction.Transactional;
import com.mooo.devforin.appservice.controller.qna.dto.*;
import com.mooo.devforin.appservice.domain.entity.QnaChat;
import com.mooo.devforin.appservice.domain.entity.QnaHistory;
import com.mooo.devforin.appservice.domain.entity.QnaSource;
import com.mooo.devforin.appservice.domain.repository.qnachat.QnaChatRepository;
import com.mooo.devforin.appservice.service.qnachat.QnaChatService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class QnaChatServiceImpl implements QnaChatService {

    @Value("${azure.vector.disease-url}")
    private String AZURE_VECTOR_DISEASE_URL;

    @Value("${azure.vector.process-url}")
    private String AZURE_VECTOR_PROCESS_URL;

    @Value("${azure.vector.api-key}")
    private String AZURE_VECTOR_API_KEY;


    private final QnaChatRepository qnaChatRepository;

    /**
     * 신규 Chat 생성
     *
     * @param id
     * @return
     */
    @Override
    @Transactional
    public QnaChat createQnaChat(String id) {
        return qnaChatRepository.save(QnaChat.builder()
                .chatId(id)
                .createdBy(id)
                .createdAt(Timestamp.valueOf(LocalDateTime.now()))
                .useYn("Y")
                .build());
    }

    /**
     * 기존 Chat 조회
     *
     * @param chatSn
     * @return
     */
    @Override
    public QnaChat selectQnaChat(Integer chatSn) {
        return qnaChatRepository.findByIdAndUseYn(chatSn, "Y")
                .orElse(null); // 값이 없으면 null 반환
    }

    /**
     * Assistant 채팅 목록 조회
     *
     * @param id
     * @param pageNum
     * @return
     */
    @Override
    public QnaChatListResponseDTO getChatList(String id, int pageNum) {
        return qnaChatRepository.getChatList(id, pageNum);
    }

    @Override
    @Transactional
    public void updateQnaChat(String id, QnaChat qnaChat) {

        qnaChat.setUpdatedBy(id);
        qnaChat.setUpdatedAt(Timestamp.valueOf(LocalDateTime.now()));

        QnaChat qnaChatForUpdate = qnaChatRepository.save(qnaChat);

        if (ObjectUtils.isEmpty(qnaChatForUpdate)) {
            log.error("qnaChat Update Fail. check origin qnaChat : {}", qnaChat);
        }
    }

    /**
     * 대화 이력 상세 조회
     *
     * @param chatSn
     * @param chatHistoryCount
     * @return
     */
    @Override
    public ChatHistoryViewResponseDTO viewChatHistory(int chatSn, Integer chatHistoryCount) {

        List<ChatHistoryDTO> chatHistoryDtos = new ArrayList<>();
        List<QnaHistory> qnaHistories = qnaChatRepository.selectChatHistoryList(chatSn);

        if (ObjectUtils.isEmpty(qnaHistories)) {
            log.error("요청 정보에 대한 QnaHistory 정보가 존재하지 않음");
            return null;
        }

        List<Integer> historySnList = new ArrayList<>();

        for (QnaHistory qh : qnaHistories) {
            historySnList.add(qh.getHistorySn());
        }

        List<QnaSource> qnaSourceList = qnaChatRepository.selectChatSourceList(historySnList);

        List<QnaSourceResponseDTO> sourceDto = new ArrayList<>();

        if (!ObjectUtils.isEmpty(qnaSourceList)) {
            List<String> processIds = new ArrayList<>();
            List<String> diseaseIds = new ArrayList<>();

            for (QnaSource source : qnaSourceList) {
                if ("uw_flow_process".equals(source.getDocType())) {
                    processIds.add(source.getId());
                }

                if ("uw_flow_disease".equals(source.getDocType())) {
                    diseaseIds.add(source.getId());
                }
            }

            // 헤더 설정
            HttpHeaders headers = new HttpHeaders();
            headers.set("api-key", AZURE_VECTOR_API_KEY);

            if (!ObjectUtils.isEmpty(processIds)) {

                URI uri = UriComponentsBuilder
                        .fromUriString(AZURE_VECTOR_PROCESS_URL)
                        .queryParam("api-version", "2024-07-01")
                        .encode()
                        .build()
                        .toUri();

                RestTemplate restTemplate = new RestTemplate();

                String processIdsJoined = processIds.stream()
                        .collect(Collectors.joining(","));
                String processFilter = String.format("search.in(id, '%s', ',')", processIdsJoined);


                VectorDBRequestDTO dbDto = new VectorDBRequestDTO(processFilter, "id,doc_title,content_for_rag");
                HttpEntity<VectorDBRequestDTO> requestEntity = new HttpEntity<>(dbDto, headers);

                ResponseEntity<VectorDBResponseDTO> response = restTemplate.exchange(uri, HttpMethod.POST, requestEntity, VectorDBResponseDTO.class);

                if (!ObjectUtils.isEmpty(response)) {
                    for (VectorDBResponseDTO.Doc vdrd : response.getBody().getValue()) {
                        sourceDto.add(QnaSourceResponseDTO.builder()
                                .title(vdrd.getDoc_title())
                                .content(vdrd.getContent_for_rag())
                                .build());
                    }
                }
            }

            if (!ObjectUtils.isEmpty(diseaseIds)) {
                URI uri = UriComponentsBuilder
                        .fromUriString(AZURE_VECTOR_DISEASE_URL)
                        .queryParam("api-version", "2024-07-01")
                        .encode()
                        .build()
                        .toUri();


                RestTemplate restTemplate = new RestTemplate();

                String diseaseIdsJoined = diseaseIds.stream()
                        .collect(Collectors.joining(","));
                String diseaseFilter = String.format("search.in(id, '%s', ',')", diseaseIdsJoined);


                VectorDBRequestDTO dbDto = new VectorDBRequestDTO(diseaseFilter, "id,doc_title,content_for_rag");
                HttpEntity<VectorDBRequestDTO> requestEntity = new HttpEntity<>(dbDto, headers);

                ResponseEntity<VectorDBResponseDTO> response = restTemplate.exchange(uri, HttpMethod.POST, requestEntity, VectorDBResponseDTO.class);

                if (!ObjectUtils.isEmpty(response)) {
                    for (VectorDBResponseDTO.Doc vdrd : response.getBody().getValue()) {
                        sourceDto.add(QnaSourceResponseDTO.builder()
                                .title(vdrd.getDoc_title())
                                .content(vdrd.getContent_for_rag())
                                .build());
                    }
                }
            }

        }
        int firstNum = 0;

        for (QnaHistory qnaHistory : qnaHistories) {
            chatHistoryDtos.add(ChatHistoryDTO.builder()
                    .historySn(qnaHistory.getHistorySn())
                    .userQuery(qnaHistory.getUserQuery())
                    .answer(qnaHistory.getAnswer())
                    .answerRevaluation(qnaHistory.getAnswerRevaluation())
                    .feedbackSn(qnaHistory.getFeedbackSn())
                    .createdAt(qnaHistory.getCreatedAt())
                    .source(firstNum == 0 ? sourceDto : null)
                    .build());
            firstNum++;
        }

        return ChatHistoryViewResponseDTO.builder()
                .sessionId(qnaHistories.get(0).getSessionId())
                .multiTurnStatus(chatHistoryCount < 20)
                .historys(chatHistoryDtos)
                .build();

    }

    @Override
    public void deleteChatHistory(QnaChat qnaChat, String id) {
        try {
            qnaChat.setUseYn("N");
            qnaChat.setUpdatedBy(id);
            qnaChat.setUpdatedAt(Timestamp.valueOf(LocalDateTime.now()));

            qnaChatRepository.save(qnaChat);

        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }
}

