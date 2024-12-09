package com.foufou.vo;

import com.foufou.dto.TableContentDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class TextQuestionVO {
    private Long id;

    private String questionTitle;

    private Integer seqNum;

    private List<TableContentDTO> tableContentDTOList;
}
