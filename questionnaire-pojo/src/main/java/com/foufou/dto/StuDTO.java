package com.foufou.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StuDTO {
    private String stuName;

    private String stuId;

    private String major;
}
