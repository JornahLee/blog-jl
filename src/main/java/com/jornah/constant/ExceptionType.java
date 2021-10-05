package com.jornah.constant;

/**
 * @author licong
 * @date 2021/10/5 20:03
 */
public enum ExceptionType {
    // 用户登录相关异常
    // 登录权限相关
    // 用户输入相关

    NOT_LOGIN("100");
    private final String code;

    ExceptionType(String code) {
        this.code = code;
    }

    public String value() {
        return this.code;
    }
}
