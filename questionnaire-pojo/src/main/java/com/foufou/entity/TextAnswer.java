package com.foufou.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TextAnswer implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long id;

    private Long textId;

    private Long questionnaireId;

    private Long tableId;

    private String stuName;

    private String content;
}
