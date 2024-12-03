package com.foufou.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TextQuestionDTO {
    private Long id;

    private String questionTitle;

    private Long tableId;

    private String col;

    private String line;
}
