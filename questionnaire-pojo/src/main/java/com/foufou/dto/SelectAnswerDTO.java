package com.foufou.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SelectAnswerDTO {
    private Long id;

    private Long questionId;

    private String questionTitle;

    private List<OptionDTO> optionDTOList;

    private List<SelectInnerAnswerDTO> selectInnerAnswerDTOList;

    private Double score;
}
