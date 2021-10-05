package com.jornah.anno;

import com.jornah.constant.ExceptionType;
import com.jornah.exception.BusinessException;
import com.jornah.model.UserInfo;
import com.jornah.utils.WebRequestHelper;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

/**
 * @author licong
 * @date 2021/10/2 21:24
 */
@Component  //加入到IoC容器
@Aspect  //指定当前类为切面类
public class AccessControlProcessor {
    @Pointcut(value = "@annotation(com.jornah.anno.AccessControl)")
    public void pointcut() {

    }

    @Before("pointcut()")
    public void before(JoinPoint point) throws NoSuchMethodException {
        String name = point.getSignature().getName();
        MethodSignature signature = (MethodSignature) point.getSignature();
        Class<?> clazz = point.getTarget().getClass();
        Method method = clazz.getDeclaredMethod(name, signature.getParameterTypes());
//        AccessControl accessControl = method.getAnnotation(AccessControl.class);
        if (WebRequestHelper.getCurrentUserInfo().getUserId() == UserInfo.TOURIST_USER_ID) {
            throw BusinessException.of(ExceptionType.NOT_LOGIN, "请登录");
        }

        //todo 暂时不做细粒度的权限校验， 只做登录校验
//        WebRequestHelper.getCurrentUserInfo().getRoles().removeAll(Arrays.asList(accessControl.role()));
//        if (!shouldPass) {
//            throw BusinessException.withErrorCode("错误");
//        }


    }

}
