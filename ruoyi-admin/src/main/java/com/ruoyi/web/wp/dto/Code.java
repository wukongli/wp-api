package com.ruoyi.web.wp.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class Code {
    @NotNull
    private String code;
}
