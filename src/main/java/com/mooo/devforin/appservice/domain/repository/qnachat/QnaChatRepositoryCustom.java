package com.mooo.devforin.appservice.domain.repository.qnachat;

import com.mooo.devforin.appservice.controller.qna.dto.QnaChatListResponseDTO;
import com.mooo.devforin.appservice.domain.entity.QnaHistory;
import com.mooo.devforin.appservice.domain.entity.QnaSource;

import java.util.List;

public interface QnaChatRepositoryCustom {

    /**
     * Assistant 채팅 목록 조회
     *
     * @param id
     * @param pageNum
     * @return
     */
    QnaChatListResponseDTO getChatList(String id, int pageNum);

    /**
     * 질문 & 답변 목록 조회
     *
     * @param chatSn
     * @return
     */
    List<QnaHistory> selectChatHistoryList(int chatSn);

    /**
     * chunk 목록 조회
     *
     * @param historySnList
     * @return
     */
    List<QnaSource> selectChatSourceList(List<Integer> historySnList);
}
