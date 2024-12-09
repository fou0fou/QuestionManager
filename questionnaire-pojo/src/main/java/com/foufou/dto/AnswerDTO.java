package com.foufou.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AnswerDTO {

    private Long questionnaireId;

    private Long questionId;

    // 0：选择   1：填空
    private Integer questionType;

    private List<AnswerContentDTO> answerContent;

    private String stuName;
}
