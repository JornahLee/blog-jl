package com.jornah.interceptor;

import com.jornah.model.UserInfo;
import com.jornah.utils.JwtUtil;
import com.jornah.utils.WebRequestHelper;
import io.jsonwebtoken.Claims;
import io.opentracing.Tracer;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 自定义拦截器
 */
@Slf4j
public class BaseInterceptor implements HandlerInterceptor {
    private final Tracer tracer;

    public BaseInterceptor(Tracer tracer) {
        this.tracer = tracer;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object o) throws Exception {
        setTraceIdInHeader(response);

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

    private void setTraceIdInHeader(HttpServletResponse response) {
        String traceId = tracer.activeSpan().context().toTraceId();
        response.setHeader("trace-id", traceId);
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object o, ModelAndView view) throws Exception {

    }


    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {

    }
}
