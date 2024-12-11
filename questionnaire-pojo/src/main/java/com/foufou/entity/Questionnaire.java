package com.foufou.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Questionnaire implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long id;

    private Long userId;

    private String title;

    private String major;

    private Integer grade;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;

    private LocalDateTime startTime;

    private LocalDateTime endTime;

    private Integer status;

    //填写次数
    private Integer fillCount;

    //内容介绍
    private String description;
}
