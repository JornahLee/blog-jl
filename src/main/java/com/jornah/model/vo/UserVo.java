package com.jornah.model.vo;

import lombok.Data;

/**
 * @author licong
 * @date 2021/10/3 10:06
 */
@Data
public class UserVo {
    /**
     * 用户名
     */
    private String username;
    /**
     * email
     */
    private String email;
    /**
     * 主页地址
     */
    private String homeUrl;
    /**
     * 用户显示的名称
     */
    private String screenName;
    /**
     * 头像
     */
    private String avatar;
    /**
     * accessToken
     */
    private String accessToken;
}
