package com.jornah.model.qo;

import io.swagger.annotations.ApiParam;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @author licong
 * @date 2021/10/3 09:31
 */
@Data
public class LoginQo {
    @ApiParam(name = "username", value = "用户名", required = true)
    @NotNull
    private String username;
    @ApiParam(name = "password", value = "用户名", required = true)
    @NotNull
    private String password;
    @ApiParam(name = "remember_me", value = "记住我", required = false)
    private String rememberMe;
}
