package com.jornah.advice;


import com.jornah.constant.ExceptionType;
import com.jornah.exception.BusinessException;
import com.jornah.utils.APIResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.servlet.http.HttpServletResponse;

@ControllerAdvice
@Slf4j
public class Advice {
    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public APIResponse<?> handleException(Exception e) {
        log.error("系统错误", e);
        return APIResponse.fail("系统错误," + e.getMessage());
    }

    @ExceptionHandler(value = BusinessException.class)
    @ResponseBody
    public APIResponse<?> handleBusinessException(BusinessException e, HttpServletResponse response) {
        if (ExceptionType.NOT_LOGIN.equals(e.getExceptionType())) {
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
        } else {
            response.setStatus(444);
        }
        log.error("业务错异常,{}", e.getErrorCode());
        return APIResponse.fail("系统错误," + e.getMessage());
    }
}
