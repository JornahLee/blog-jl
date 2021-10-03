package com.jornah.model;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @author licong
 * @date 2021/10/2 21:03
 */
@Data
public class UserInfo {
    private Long userId;
    private List<Role> roles=new ArrayList<>();

    public static UserInfo tourist() {
        UserInfo userInfo = new UserInfo();
        userInfo.setUserId(-1L);
        userInfo.roles.add(Role.ADMIN);
        userInfo.roles.add(Role.TOURIST);
        return userInfo;
    }
}
