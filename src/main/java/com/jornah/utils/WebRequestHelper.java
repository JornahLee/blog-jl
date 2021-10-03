package com.jornah.utils;

import com.jornah.model.UserInfo;
import io.jsonwebtoken.Claims;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

import java.util.Objects;

/**
 * @author licong
 * @date 2021/10/2 20:59
 */
public class WebRequestHelper {
    private static final String USER_INFO_KEY = "userInfo";

    private WebRequestHelper() {

    }

    public static UserInfo getCurrentUserInfo() {
        RequestAttributes requestAttributes = RequestContextHolder.currentRequestAttributes();
        Object userInfo = requestAttributes.getAttribute(USER_INFO_KEY, requestAttributes.SCOPE_REQUEST);
        return Objects.nonNull(userInfo) ? (UserInfo) userInfo : UserInfo.tourist();
    }

    public static void setCurrentUserInfo(UserInfo userInfo) {
        RequestAttributes requestAttributes = RequestContextHolder.currentRequestAttributes();
        requestAttributes.setAttribute(USER_INFO_KEY, userInfo, requestAttributes.SCOPE_REQUEST);
    }

    public static void setCurrentUserInfo(Claims claims) {
        Integer userId = (Integer) claims.get("userId");
        UserInfo userInfo = new UserInfo();
        userInfo.setUserId(userId.longValue());
        setCurrentUserInfo(userInfo);
    }

}
