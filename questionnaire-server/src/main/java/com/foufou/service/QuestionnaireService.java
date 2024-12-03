package com.foufou.service;

import com.foufou.dto.QuestionnaireDTO;
import com.foufou.results.PageResult;
import com.foufou.vo.QuestionnaireDetailVO;
import com.foufou.vo.QuestionnaireVO;

public interface QuestionnaireService {
    void createPaper(QuestionnaireDTO questionnaireDTO);

    void deletePaper(Long id);

    void updatePaper(QuestionnaireDTO questionnaireDTO);

    PageResult pageSelect(String title, Integer page, Integer pageSize, Long userId);

    QuestionnaireVO getPaper(Long id);

    QuestionnaireDetailVO getWholePaper(Long id);
}
