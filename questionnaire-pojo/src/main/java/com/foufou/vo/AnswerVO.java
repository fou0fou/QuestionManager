package com.foufou.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AnswerVO {
//    private Long id;

    private Long questionnaireId;

    private Long questionId;

    // 0：选择   1：填空
    private Integer questionType;

    private String answerContent;

    private String stuName;
}
