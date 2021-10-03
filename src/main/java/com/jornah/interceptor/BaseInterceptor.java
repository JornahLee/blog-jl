package com.jornah.interceptor;

import com.jornah.model.UserInfo;
import com.jornah.utils.JwtUtil;
import com.jornah.utils.WebRequestHelper;
import io.jsonwebtoken.Claims;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 自定义拦截器
 */
@Component
public class BaseInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object o) throws Exception {
        String auth = request.getHeader("Authorization");
        if (StringUtils.isBlank(auth)) {
            WebRequestHelper.setCurrentUserInfo(UserInfo.tourist());
            return true;
        }
        Claims claims = JwtUtil.getSingleton().parseToken(auth);
//        entries.
        WebRequestHelper.setCurrentUserInfo(claims);
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object o, ModelAndView view) throws Exception {
    }


    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {

    }
}
