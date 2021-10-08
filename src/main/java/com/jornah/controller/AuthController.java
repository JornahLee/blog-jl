package com.jornah.controller;

import com.google.common.collect.ImmutableMap;
import com.jornah.model.converter.UserConverter;
import com.jornah.model.entity.User;
import com.jornah.model.qo.LoginQo;
import com.jornah.model.vo.UserVo;
import com.jornah.service.user.UserService;
import com.jornah.utils.APIResponse;
import com.jornah.utils.JwtUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

@Api("登录相关接口")
@RestController
@RequestMapping("/blog/auth")
@CrossOrigin
public class AuthController extends BaseController {

    @Autowired
    private UserService userService;


    @ApiOperation("登录")
    @PostMapping(value = "/login")
    public APIResponse<UserVo> login(@RequestBody  LoginQo loginQo) {
        // 调用Service登录方法
        User userInfo = userService.login(loginQo.getUsername(), loginQo.getPassword());
        UserVo userVo = UserConverter.INSTANCE.toVo(userInfo);
        String accessToken = JwtUtil.getSingleton().generateToken(
                ImmutableMap.of("userId", userInfo.getId()),
                Instant.now().plus(30, ChronoUnit.DAYS));
        userVo.setAccessToken(accessToken);
        return APIResponse.success(userVo);
    }

    @RequestMapping(value = "/logout")
    public void logout() {
        // jwt 无状态 无法使token过期
        // 暂时前端直接删掉token即可
    }


}
