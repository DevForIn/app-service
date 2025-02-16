package com.mooo.devforin.appservice.domain.repository.qnasource;

import com.mooo.devforin.appservice.domain.entity.QnaChat;
import com.mooo.devforin.appservice.domain.entity.QnaSource;

import java.util.List;

public interface QnaSourceRepositoryCustom {

    List<QnaSource> requestQnaSource(QnaChat qnaChat);

}
