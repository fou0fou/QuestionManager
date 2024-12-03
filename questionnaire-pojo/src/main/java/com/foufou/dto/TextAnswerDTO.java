package com.foufou.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TextAnswerDTO {
    private Long id;

    private Long questionId;

    private String col;

    private String line;

    private Double content;
}
