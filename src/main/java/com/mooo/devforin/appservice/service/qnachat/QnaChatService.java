package com.mooo.devforin.appservice.service.qnachat;

import com.mooo.devforin.appservice.controller.qna.dto.ChatHistoryViewResponseDTO;
import com.mooo.devforin.appservice.controller.qna.dto.QnaChatListResponseDTO;
import com.mooo.devforin.appservice.domain.entity.QnaChat;

public interface QnaChatService {

    /**
     * 신규 Chat 생성
     * 
     * @param id
     * @return
     */
    QnaChat createQnaChat(String id);

    /**
     * 기존 Chat 조회
     *
     * @param chatSn
     * @return
     */
    QnaChat selectQnaChat(Integer chatSn);

    /**
     * Assistant 채팅 목록 조회
     *
     * @param id
     * @param pageNum
     * @return
     */
    QnaChatListResponseDTO getChatList(String id, int pageNum);

    /**
     * 멀티턴 대화 시 업데이트
     * @param id
     * @param qnaChat
     */
    void updateQnaChat(String id, QnaChat qnaChat);

    /**
     * 대화 이력 상세 조회
     *
     * @param chatSn
     * @param chatHistoryCount
     * @return
     */
    ChatHistoryViewResponseDTO viewChatHistory(int chatSn, Integer chatHistoryCount);

    /**
     * 대화 이력 삭제 처리
     * @param qnaChat
     * @param id
     * @return
     */
    void deleteChatHistory(QnaChat qnaChat,String id);
}

