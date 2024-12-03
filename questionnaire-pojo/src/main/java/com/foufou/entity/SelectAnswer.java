package com.foufou.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SelectAnswer implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long id;

    private Long selectId;

    private Long questionnaire_id;

    private String stuName;

    private List<Option> options;
}