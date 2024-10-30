package com.foufou.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Question implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    private Long questionnaireId;

    private String questionTitle;

    // 单选、多选、填空、文件
    private String questionType;

    private List<String> optionDescription;

    //逻辑删除，要删除一条数据的话不会去真删他，而是把逻辑删除字段从0置成1
    private Integer deleted;

}
