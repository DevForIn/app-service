package com.mooo.devforin.appservice.service.qnafeedback;

import com.mooo.devforin.appservice.controller.qna.dto.FeedbackDTO;
import com.mooo.devforin.appservice.controller.qna.dto.FeedbackViewResponseDTO;
import com.mooo.devforin.appservice.domain.entity.QnaFeedback;
import com.mooo.devforin.appservice.domain.entity.QnaHistory;

public interface QnaFeedbackService {

    QnaFeedback createFeedback(QnaHistory qnaHistory, FeedbackDTO dto, String id);

    FeedbackViewResponseDTO ViewFeedback(Integer feedbackSn);
}
