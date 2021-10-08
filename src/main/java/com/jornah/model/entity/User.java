package com.jornah.model.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * 用户表
 */
@Data
public class User extends BaseEntity implements Serializable {

    /** 用户名 */
    private String username;
    /** 密码 */
    private String password;
    /** email */
    private String email;
    /** 主页地址 */
    private String homeUrl;
    /**  用户显示的名称 */
    private String screenName;
}
