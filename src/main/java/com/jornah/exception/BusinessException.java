/**
 * Created by IntelliJ IDEA.
 * User: Jornah Lee
 * DateTime: 2018/7/23 14:39
 **/
package com.jornah.exception;

import com.jornah.constant.ExceptionType;
import lombok.Data;

/**
 * 统一异常类
 */
@Data
public class BusinessException extends RuntimeException {

    protected String errorCode;
    protected ExceptionType exceptionType;
    protected String[] errorMessageArguments;

    protected BusinessException() {
        this("");
    }

    public BusinessException(String message) {
        super(message);
        this.errorCode = "fail";
        this.errorMessageArguments = new String[0];
    }

    public BusinessException(String message, Throwable cause) {
        super(message, cause);
        this.errorCode = "fail";
        this.errorMessageArguments = new String[0];
    }

    public static BusinessException of(String errorCode) {
        BusinessException businessException = new BusinessException();
        businessException.errorCode = errorCode;
        return businessException;
    }

    public static BusinessException of(ExceptionType exceptionType, String message) {
        BusinessException businessException = new BusinessException(message);
        businessException.errorCode = exceptionType.value();
        businessException.exceptionType = exceptionType;
        return businessException;
    }

}
