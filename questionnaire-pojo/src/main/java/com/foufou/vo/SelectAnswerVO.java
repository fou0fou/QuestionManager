package com.foufou.vo;

import com.foufou.dto.OptionDTO;
import com.foufou.dto.SelectInnerAnswerDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SelectAnswerVO {
    private Integer seqNum;

    private Double score;

    private String questionTitle;

    private List<SelectInnerAnswerDTO> selectInnerAnswerDTOList;
}
