package com.foufou.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TextAnswer implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long id;

    private Long textId;

    private Long questionnaire_id;

    private String stuName;

    private String stuId;

    private String content;
}
