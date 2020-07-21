/**
 * Created by IntelliJ IDEA.
 * User: Jornah Lee
 * DateTime: 2018/8/3 16:30
 **/
package com.wip.model.dto;

import java.io.Serializable;

/**
 * 公共属性的类
 */
public class BaseDto implements Serializable {

    /**
     * 用户名
     */
    private String userName;

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserName() {
        return userName;
    }
}
