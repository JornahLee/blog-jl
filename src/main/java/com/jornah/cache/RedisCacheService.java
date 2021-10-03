package com.jornah.cache;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;

/**
 * @author licong
 * @date 2021/10/3 09:38
 */
@Service
@Slf4j
public class RedisCacheService implements CacheService {

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Override
    public void setValueIfAbsent(String key, String value, Duration timeout) {
        redisTemplate.opsForValue().setIfAbsent(key, value, timeout);
    }

    @Override
    public void setValueIfAbsent(String key, String value) {
        redisTemplate.opsForValue().setIfAbsent(key, value);
    }

    @Override
    public <T> T getValue(String key, Class<T> requireType) {
        String str = redisTemplate.opsForValue().get(key);
        if (Integer.class.getName().equals(requireType.getName())) {
            return (T) Integer.valueOf(str);
        }
        if (Long.class.getName().equals(requireType.getName())) {
            return (T) Long.valueOf(str);
        }
        return (T) str;
    }

    @Override
    public Long increment(String key) {
        return redisTemplate.opsForValue().increment(key);

    }

}
