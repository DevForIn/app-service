package com.mooo.devforin.appservice.service.qnasource.impl;

import jakarta.transaction.Transactional;
import com.mooo.devforin.appservice.controller.qna.dto.QnaAnswerResponseDTO;
import com.mooo.devforin.appservice.controller.qna.dto.QnaSourceResponseDTO;
import com.mooo.devforin.appservice.domain.entity.QnaChat;
import com.mooo.devforin.appservice.domain.entity.QnaHistory;
import com.mooo.devforin.appservice.domain.entity.QnaSource;
import com.mooo.devforin.appservice.domain.repository.qnasource.QnaSourceRepository;
import com.mooo.devforin.appservice.service.qnasource.QnaSourceService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class QnaSourceServiceImpl implements QnaSourceService {

    @Value("${azure.vector.disease-url}")
    private String AZURE_VECTOR_DISEASE_URL;

    @Value("${azure.vector.process-url}")
    private String AZURE_VECTOR_PROCESS_URL;

    @Value("${azure.vector.api-key}")
    private String AZURE_VECTOR_API_KEY;

    private final QnaSourceRepository qnaSourceRepository;

    /**
     * 사용자 문의 - 출처(chunk data) 데이터 생성
     *
     * @param qnaHistory
     * @param dto
     * @return
     */
    @Override
    @Transactional
    public List<QnaSourceResponseDTO> createQnaSource(QnaHistory qnaHistory, ResponseEntity<QnaAnswerResponseDTO> dto) {
        log.info("DB update to Source . . .");
        List<QnaSourceResponseDTO> sourceLists = new ArrayList<>();
        List<QnaSource> sourceDBSaveList =  new ArrayList<>();

        List<QnaAnswerResponseDTO.Data> dtoData = dto.getBody().getResult().getData();

        for(int i = 0; i< dtoData.size(); i++) {
            try {
                for (int j = 0; j < dtoData.get(i).getDocResult().getValue().size(); j++) {

                    String intent = null;

                    if (!"none".equals(dtoData.get(i).getDiseaseIntent()) || !"none".equals(dtoData.get(i).getProductIntent())) {
                        intent = "none".equals(dtoData.get(i).getDiseaseIntent())
                                ? dtoData.get(i).getProductIntent()
                                : dtoData.get(i).getDiseaseIntent();
                    }
                    sourceLists.add(QnaSourceResponseDTO.builder()
                                    .title(dtoData.get(i).getDocResult().getValue().get(j).getDoc_title())
                                    .chunkName(dtoData.get(i).getDocResult().getValue().get(j).getChunk_name())
                                    .intent(intent)
                                    .content(dtoData.get(i).getDocResult().getValue().get(j).getContent_for_rag())

                            .build());

                    QnaSource qnaSource = new QnaSource();
                    qnaSource.setQnaHistory(qnaHistory);
                    qnaSource.setId(dtoData.get(i).getDocResult().getValue().get(j).getId());
                    qnaSource.setDocType(dtoData.get(i).getDocResult().getOdata_context().contains("uw_flow_process")? "uw_flow_process" : "uw_flow_disease");
                    qnaSource.setCreatedBy(qnaHistory.getCreatedBy());
                    qnaSource.setCreatedAt(Timestamp.valueOf(LocalDateTime.now()));

                    sourceDBSaveList.add(qnaSource);
                }

            } catch (Exception e) {
                log.info("Doc Result is null, Not Error !");
            }
        }

        // 중복 제거
        LinkedHashSet<QnaSourceResponseDTO> removeSameResult= new LinkedHashSet<>(sourceLists);
        LinkedHashSet<QnaSource> removeSameResultForDB= new LinkedHashSet<>(sourceDBSaveList);

        for(QnaSource saveQnaSource : removeSameResultForDB){
            qnaSourceRepository.save(saveQnaSource);
        }

        log.info("DB update Done !");
        return new ArrayList<>(removeSameResult);
    }

    @Override
    public List<QnaSource> requestQnaSource(QnaChat qnaChat) {
        return qnaSourceRepository.requestQnaSource(qnaChat);
    }
}

