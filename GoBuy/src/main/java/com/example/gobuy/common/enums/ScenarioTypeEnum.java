package com.example.gobuy.common.enums;

import lombok.Getter;

@Getter
public enum ScenarioTypeEnum {

    GAMING(1, "电竞游戏"),
    OFFICE(2, "办公生产力"),
    CREATIVE(3, "创意设计"),
    STUDY(4, "学习编程"),
    HOME(5, "家庭娱乐");

    private final int code;
    private final String description;

    ScenarioTypeEnum(int code, String description) {
        this.code = code;
        this.description = description;
    }
}
