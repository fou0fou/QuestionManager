package com.foufou.vo;

import com.foufou.dto.SelectQuestionDTO;
import com.foufou.dto.TextQuestionDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class QuestionnaireDetailVO {
    private Long id;

    private Long userId;

    private String title;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;

    private LocalDateTime startTime;

    private LocalDateTime endTime;

    private Integer status;

    private Integer fillCount;

    private String description;

    private List<SelectQuestionDTO> selectQuestionList;

    private List<TextQuestionDTO> textQuestionList;
}
