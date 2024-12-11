package com.foufou.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TextAnswerVO {
    private Integer seqNum;

    private String questionTitle;

    private List<String> answers;
}
