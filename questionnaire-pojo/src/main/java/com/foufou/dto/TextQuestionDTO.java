package com.foufou.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TextQuestionDTO {
    private Long id;

    private String questionTitle;

    private List<TableContentDTO> tableContentDTOList;
}
