package com.ruoyi.web.wp.dto;

import lombok.Data;

import java.util.ArrayList;


@Data
public class ParseResult {
    private String errno;
    private String realLink;
    private ArrayList<Link> list;
    private String code;
    private String codeUseNum;
}
