package com.foufou.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

@Data
@AllArgsConstructor
public class Option implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long questionId;

    private Integer sequence;  //在这个问题中是第几个选项

    private String content;
}
