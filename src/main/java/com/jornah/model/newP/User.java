package com.jornah.model.newP;

import lombok.Data;

import java.io.Serializable;
import java.time.Instant;

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
    /** 最后活动时间 */
    private Instant activated;
    /** 上次登录最后活跃时间 */
    private Integer logged;
    /** 用户组 */
    private String groupName;

}
