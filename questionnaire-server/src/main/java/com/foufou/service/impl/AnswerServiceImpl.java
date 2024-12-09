package com.foufou.service.impl;

import com.foufou.dto.AnswerDTO;
import com.foufou.entity.SelectAnswer;
import com.foufou.entity.TextAnswer;
import com.foufou.mapper.SelectQuestionAnswerMapper;
import com.foufou.mapper.TextQuestionAnswerMapper;
import com.foufou.service.AnswerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AnswerServiceImpl implements AnswerService {
    @Autowired
    private SelectQuestionAnswerMapper selectQuestionAnswerMapper;
    @Autowired
    private TextQuestionAnswerMapper textQuestionAnswerMapper;

    @Override
    public void addAnswer(List<AnswerDTO> answerDTOs) {
        answerDTOs.forEach(answerDTO -> {
            if (answerDTO.getQuestionType() == 0) { // 选择题
                answerDTO.getAnswerContent().forEach(answerContentDTO -> {
                    SelectAnswer selectAnswer = SelectAnswer.builder()
                            .selectInnerId(answerContentDTO.getId())
                            .selectId(answerDTO.getQuestionId())
                            .questionnaireId(answerDTO.getQuestionnaireId())
                            .stuName(answerDTO.getStuName())
                            .content(answerContentDTO.getAnswer())
                            .build();
                    selectQuestionAnswerMapper.addAnswer(selectAnswer);
                });
            } else if (answerDTO.getQuestionType() == 1) {  //填空题
                if (null == answerDTO.getAnswerContent().get(0).getId()) {  // 精简填空题
                    TextAnswer textAnswer = TextAnswer.builder()
                            .textId(answerDTO.getQuestionId())
                            .questionnaireId(answerDTO.getQuestionnaireId())
                            .stuName(answerDTO.getStuName())
                            .content(answerDTO.getAnswerContent().get(0).getAnswer())
                            .build();
                    textQuestionAnswerMapper.addTextAnswer(textAnswer);
                } else {    // 表题
                    answerDTO.getAnswerContent().forEach(answerContentDTO -> {
                        TextAnswer textAnswer = TextAnswer.builder()
                                .textId(answerDTO.getQuestionId())
                                .questionnaireId(answerDTO.getQuestionnaireId())
                                .stuName(answerDTO.getStuName())
                                .tableId(answerContentDTO.getId())
                                .content(answerContentDTO.getAnswer())
                                .build();
                        textQuestionAnswerMapper.addTableAnswer(textAnswer);
                    });
                }
            }
        });
    }
}
