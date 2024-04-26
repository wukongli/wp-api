package com.ruoyi.web.utils;

public enum OpcEnum {

    F13D001XF01_RU("新风机组运行状态"),
    F13D001XF01_TS("新风机组送风湿度"),
    F13D001XF01_HS("新风机组送风温度");

    private String name;

    OpcEnum(String name) {
        this.name = name;
    }


    public String getName() {
        return name;
    }
}
