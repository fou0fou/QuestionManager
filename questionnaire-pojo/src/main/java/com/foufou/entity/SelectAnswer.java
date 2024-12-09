package com.foufou.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SelectAnswer implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long id;

    private Long selectId;

    private Long questionnaireId;

    private Long selectInnerId;

    private String stuName;

    private String content;


}