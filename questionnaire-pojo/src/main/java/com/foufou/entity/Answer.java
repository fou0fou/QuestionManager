package com.foufou.entity;

import java.io.Serializable;

public class Answer implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long id;

    private Long questionId;

    private String content;

    private Integer deleted;
}
