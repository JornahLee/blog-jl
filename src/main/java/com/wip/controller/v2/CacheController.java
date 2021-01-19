package com.wip.controller.v2;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

@RestController
@RequestMapping("/cache")
@CrossOrigin
public class CacheController {
    @Autowired
    private StringRedisTemplate redisTemplate;

    @GetMapping("/get/{key}")
    public String get(@PathVariable("key") String key) {
        return redisTemplate.opsForValue().get(key);
    }
    @PostMapping("/set")
    @ResponseStatus(HttpStatus.CREATED)
    public void set(@RequestParam String key,@RequestParam String value) {
        redisTemplate.opsForValue().set(key,value);
    }

    @RequestMapping("/cache/flush")
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
