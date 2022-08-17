package com.jornah.advice;


import com.jornah.exception.BusinessException;
import com.jornah.utils.APIResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.stream.Collectors;

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

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public APIResponse<?> validExceptionHandler(MethodArgumentNotValidException e) {
        List<FieldError> fieldErrors = e.getBindingResult().getFieldErrors();
        String msg = fieldErrors.stream()
                .map(fieldError -> fieldError.getField() + ":" + fieldError.getDefaultMessage())
                .collect(Collectors.joining(","));
        return APIResponse.fail(msg);
    }

    @ExceptionHandler(value = BusinessException.class)
    @ResponseBody
    public APIResponse<?> handleBusinessException(BusinessException e, HttpServletResponse response) {
        switch (e.getExceptionType()) {
            case NOT_LOGIN:
            case TOKEN_EXPIRED:
                response.setStatus(HttpStatus.UNAUTHORIZED.value());
                break;
            default:
                response.setStatus(HttpStatus.FORBIDDEN.value());
        }
        log.error("业务错异常,{}", e.getErrorCode(),e);
        return APIResponse.fail(e.getMessage());
    }
}
