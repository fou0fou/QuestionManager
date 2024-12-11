package com.foufou.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class StuQuestionnaireDetailVO {
    private Long id;

    private String title;

    private String description;

    private List<SelectQuestionVO> selectQuestionList;

    private List<TextQuestionVO> textQuestionList;
}
