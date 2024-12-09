package com.foufou.service;

import com.foufou.dto.TextQuestionDTO;
import com.foufou.dto.TextSimpleQuestionDTO;

public interface TextQuestionService {
    Long addTextQuestion(Long questionnaireId, Integer seqNum);

    void updateTextQuestion(TextSimpleQuestionDTO textSimpleQuestionDTO);

    void updateTableQuestion(TextQuestionDTO textQuestionDTO);

    void deleteQuestionById(Long questionnaireId, Long questionId);
}
