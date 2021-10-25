package com.jornah.cache;

import java.time.Duration;

public interface CacheService {
    void setValueIfAbsent(String key, String value, Duration timeout);

    void setValueIfAbsent(String key, String value);

    void setValue(String key, String value);

    <T> T getValue(String key, Class<T> requireType);

    Long increment(String key);
}
