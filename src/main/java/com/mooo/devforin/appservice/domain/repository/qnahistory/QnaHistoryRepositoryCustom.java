package com.mooo.devforin.appservice.domain.repository.qnahistory;

import com.mooo.devforin.appservice.domain.entity.QnaChat;

public interface QnaHistoryRepositoryCustom {

    /**
     * 채팅 대화 내역 수 조회
     * @param qnaChat
     * @return
     */
    Integer getQnaHistoryListCount(QnaChat qnaChat);

}
