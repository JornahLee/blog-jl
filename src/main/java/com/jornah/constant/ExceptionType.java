package com.jornah.constant;

/**
 * @author licong
 * @date 2021/10/5 20:03
 */
public enum ExceptionType {
    // 用户登录相关异常
    // 登录权限相关
    // 用户输入相关E

    NOT_LOGIN(100),
    TOKEN_EXPIRED(101),
    INVALID_PASSWORD(102),
    TOO_MANY_REQUESTS(103),
    BAD_VERSION(104);


    private final int code;

    ExceptionType(int code) {
        this.code = code;
    }

    public int value() {
        return this.code;
    }
}
