package com.foufou.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TableAnswerVO {
    private Integer seqNum;

    private String questionTitle;

    private List<SingleTableAnswer> sigleTableAnswerList;
}
