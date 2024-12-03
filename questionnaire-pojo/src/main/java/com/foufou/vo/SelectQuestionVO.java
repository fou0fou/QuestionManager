package com.foufou.vo;

import com.foufou.dto.OptionDTO;
import com.foufou.dto.SelectInnerQuestionDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SelectQuestionVO {
    private Long id;

    private String title;

    private List<SelectInnerQuestionDTO> selectInnerQuestionDTOList;

    private List<OptionDTO> optionDTOList;
}
