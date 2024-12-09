package com.foufou.service;

import com.foufou.dto.SelectQuestionDTO;

public interface SelectQuestionService {
    Long addSelectQuestion(Long questionnaireId, Integer seqNum);

    void updateQuestion(SelectQuestionDTO selectQuestionDTO);

    void deleteQuestion(Long questionnaireId, Long questionId);
}
