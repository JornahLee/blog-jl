package com.jornah.service.cache;

import org.redisson.api.RList;

import java.util.List;

public interface CacheService<ID, T extends Cacheable<ID>> {
    T get(ID id);

    boolean remove(ID id);

    void save(T t);

    RList<T> saveList(List<T> list);
    void clearList();
}
