package com.wip.controller;

import com.tencentcloudapi.common.exception.TencentCloudSDKException;
import com.tencentcloudapi.sms.v20190711.models.SendSmsResponse;
import com.wip.utils.APIResponse;
import com.wip.utils.SMSUtil;
import org.apache.commons.lang3.RandomUtils;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@RestController
@RequestMapping("/sms")
public class SMSController {
    public static final String EFFORT_TIME = "5";
    public static final Map<String, String> verifyCodeCache = new HashMap<>();

    @GetMapping("/send/{phoneNumber}")
    public APIResponse sendVerifyCode(@PathVariable String phoneNumber) throws TencentCloudSDKException {
        String verifyCode = generateVerifyCode();
        verifyCodeCache.put(phoneNumber, verifyCode);
        SendSmsResponse sendSmsResponse = SMSUtil.SendSms(SMSUtil.defaultClient(), SMSUtil.defaultRequest(verifyCode, EFFORT_TIME, "+86" + phoneNumber));
        return APIResponse.success(sendSmsResponse);
    }

    private String generateVerifyCode() {
        Integer code = RandomUtils.nextInt(1000, 9999);
        return code.toString();
    }

    @GetMapping("/verify/{phoneNumber}/{verifyCode}")
    public APIResponse verify(@PathVariable String phoneNumber, @PathVariable String verifyCode) {
        String s = verifyCodeCache.get(phoneNumber);
        verifyCodeCache.remove(phoneNumber);
        if (Objects.equals(s, verifyCode)) {
            return APIResponse.success("verify pass");
        }
        return APIResponse.success("verify error");
    }
}
