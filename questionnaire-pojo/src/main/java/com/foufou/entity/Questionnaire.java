package com.foufou.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
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

    private String createUsername;

    private Date createTime;

    private Date updateTime;

    //1可填，0不可填
    private Integer status;

    //填写次数
    private Integer fillCount;

    //内容介绍
    private String Description;

    private Integer deleted;
}
