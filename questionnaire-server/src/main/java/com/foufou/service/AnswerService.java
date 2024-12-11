package com.foufou.service;

import com.foufou.dto.AnswerDTO;
import com.foufou.vo.SelectAnswerVO;
import com.foufou.vo.TableAnswerVO;
import com.foufou.vo.TextAnswerVO;

import java.util.List;

public interface AnswerService {
    void addAnswer(List<AnswerDTO> answerDTOS);

    List<TextAnswerVO> getTextResult(Long id);

    List<TableAnswerVO> getTableResult(Long id);

    List<SelectAnswerVO> getSelectResult(Long id);
}
