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
    public static final long TOURIST_USER_ID = -1;
    private Long userId;
    private List<Role> roles = new ArrayList<>();

    public static UserInfo tourist() {
        UserInfo userInfo = new UserInfo();
        userInfo.setUserId(TOURIST_USER_ID);
        userInfo.roles.add(Role.TOURIST);
        return userInfo;
    }
}
