package com.foufou.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SelectInnerAnswerDTO {
    private String questionDetails;

    private List<String> optionContent;

    private Double evaluationScore;

    private List<Integer> counts;
}
