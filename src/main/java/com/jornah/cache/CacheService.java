package com.jornah.cache;

import org.springframework.data.redis.core.StringRedisTemplate;

import java.time.Duration;

//todo use jetcache
public interface CacheService {
    void setValueIfAbsent(String key, String value, Duration timeout);

    void setValueIfAbsent(String key, String value);

    void setValue(String key, String value);

    <T> T getValue(String key, Class<T> requireType);

    Long increment(String key);

    StringRedisTemplate redisTemplate();
}
