package com.foufou.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TextQuestion implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long id;

    private String questionTitle;

    private Long tableId;

    private String col;

    private String line;
}
