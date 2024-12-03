package com.foufou.dto;

import com.foufou.enumeration.SelectType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SelectQuestionDTO {
    private Long id;

    private String title;

    private SelectType selectType;

    private List<SelectInnerQuestionDTO> selectInnerQuestionDTOList;

    private List<OptionDTO> optionDTOList;
}
