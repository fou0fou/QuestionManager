package com.foufou.vo;

import com.foufou.dto.OptionDTO;
import com.foufou.dto.SelectInnerQuestionDTO;
import com.foufou.enumeration.SelectType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SelectQuestionVO {
    private Long id;

    private String title;

    private Integer seqNum;

    private SelectType selectType;

    private List<SelectInnerQuestionDTO> selectInnerQuestionDTOList;

    private List<OptionDTO> optionDTOList;
}
