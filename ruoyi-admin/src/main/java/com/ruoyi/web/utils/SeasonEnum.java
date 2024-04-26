package com.ruoyi.web.utils;

public enum SeasonEnum {
    SPRING("春天"), SUMMER("夏天"), AUMUTN("秋天"), WINTER("冬天");

    private String chinese;

    //枚举类型的构造函数默认为private，因为枚举类型的初始化要在当前枚举类中完成。
    SeasonEnum(String chinese) {
        this.chinese = chinese;
    }

    public String getChinese() {
        return chinese;
    }

}
