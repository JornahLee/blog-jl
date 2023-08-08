package com.jornah.controller;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.jornah.model.converter.UserConverter;
import com.jornah.model.entity.Config;
import com.jornah.model.entity.User;
import com.jornah.model.qo.LoginQo;
import com.jornah.model.qo.ReadRecord;
import com.jornah.model.vo.SiteInfo;
import com.jornah.model.vo.UserVo;
import com.jornah.service.ConfigService;
import com.jornah.service.MetaInfoService;
import com.jornah.service.user.UserService;
import com.jornah.utils.APIResponse;
import com.jornah.utils.JwtUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Api("用户接口")
@RestController
@RequestMapping("/blog/user")
@CrossOrigin
public class UserController extends BaseController {

    public static final String INDEX_STATS = "index_stats";
    @Autowired
    private UserService userService;
    @Autowired
    private MetaInfoService metaInfoService;
    @Autowired
    private ConfigService configService;


    @ApiOperation("登录")
    @PostMapping(value = "/login")
    public APIResponse<UserVo> login(@RequestBody LoginQo loginQo) {
        // 调用Service登录方法
        User userInfo = userService.login(loginQo.getUsername(), loginQo.getPassword());
        UserVo userVo = UserConverter.INSTANCE.toVo(userInfo);
        String accessToken = JwtUtil.getSingleton().generateToken(
                ImmutableMap.of("userId", userInfo.getId()),
                Instant.now().plus(30, ChronoUnit.DAYS));
        userVo.setAccessToken(accessToken);
        return APIResponse.success(userVo);
    }

    @ApiOperation("博客信息")
    @GetMapping(value = "/info")
    public APIResponse<SiteInfo> info() {
        // 调用Service登录方法
        User ownerInfo = userService.getById(1L);
        UserVo userVo = UserConverter.INSTANCE.toVo(ownerInfo);

        Config config = configService.getConfigByKey(INDEX_STATS);
        List<String> statsInfoList = Lists.newArrayList(config.getValue1(), config.getValue2(),
                config.getValue3(), config.getValue4());

        SiteInfo siteInfo = SiteInfo.from(
                userVo,
                metaInfoService.getAllCategory(),
                metaInfoService.getAllTag(),
                statsInfoList);
        return APIResponse.success(siteInfo);
    }

    @ApiOperation("注销登录")
    @RequestMapping(value = "/logout")
    public void logout() {
        // jwt 无状态 无法使token过期
        // 暂时前端直接删掉token即可
    }

    @ApiOperation("记录 最近阅读")
    @PutMapping("/recently-read")
    public void saveRecentRead(@RequestBody ReadRecord readRecord) {
        userService.saveReadRecord(readRecord);
    }

    @ApiOperation("获取最近阅读信息")
    @GetMapping("/recently-read")
    public APIResponse<List<ReadRecord>> getRecentRead() {
        return APIResponse.success(userService.getRecentRead());
    }


}
