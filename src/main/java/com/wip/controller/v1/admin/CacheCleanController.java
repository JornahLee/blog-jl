package com.wip.controller.v1.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

@RestController
@RequestMapping("/admin/cache")
public class CacheCleanController {
    @Autowired
    private StringRedisTemplate redisTemplate;

    @RequestMapping("/flush")
    public Map<String,String> cleanAllCache() {
        Map<String,String> retMap=new HashMap<>();
        Integer sizeBefore = Optional.ofNullable(redisTemplate.keys("*")).map(Set::size).orElse(0);
        retMap.put("key count before flush",sizeBefore.toString());
        redisTemplate.execute((RedisCallback<?>) connection -> {
            connection.flushAll();
            return Optional.empty();
        });
        Integer sizeAfter = Optional.ofNullable(redisTemplate.keys("*")).map(Set::size).orElse(0);
        retMap.put("key count after flush",sizeAfter.toString());
        return retMap;
    }

}
