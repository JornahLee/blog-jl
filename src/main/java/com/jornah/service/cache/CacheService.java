package com.jornah.service.cache;

import org.redisson.api.RList;

import java.util.List;
import java.util.concurrent.TimeUnit;

public interface CacheService<ID, T extends Cacheable<ID>> {
    T get(ID id);

    boolean remove(ID id);

    void save(T t);

    void saveList(List<T> list);

    void saveList(List<T> list, long timeToLive, TimeUnit timeUnit);

    List<T> getList();

    void clearList();
}
