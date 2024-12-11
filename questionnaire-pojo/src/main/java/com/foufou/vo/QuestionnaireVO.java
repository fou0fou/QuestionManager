package com.foufou.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class QuestionnaireVO {
    private Long id;

    private String major;

    private Integer grade;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;

    private LocalDateTime startTime;

    private LocalDateTime endTime;

    private Integer status;

    private Integer fillCount;

    private String description;

    private String title;
}
