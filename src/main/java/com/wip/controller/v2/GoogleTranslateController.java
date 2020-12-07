package com.wip.controller.v2;

import com.wip.controller.v2.model.vo.TranslateRequest;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.Map;
import java.util.Objects;

@RestController
@RequestMapping("/google")
public class GoogleTranslateController {
    @Autowired
    private static String secret = "my-secret-1997";

    @Autowired
    private StringRedisTemplate redisTemplate;
    RestTemplate restTemplate = new RestTemplate();

    @PostMapping("/translate")
    @CrossOrigin
    public Object translate(@RequestBody TranslateRequest request) {
        ValueOperations<String, String> opsForValue = redisTemplate.opsForValue();
        String pwd = opsForValue.get("google-api-pwd");
        boolean auth = Objects.nonNull(request.getSecret()) && StringUtils.isNotBlank(pwd) && pwd.equals(request.getSecret());
        if (!auth) {
            return "密码错误";
        }
        request.setSecret(secret);
        return restTemplate.postForEntity("http://34.92.234.192/google/translate", request, Map.class);
    }

}
