package com.jornah.service.cache;

/**
 * @author licong
 * @date 2023/2/15 15:27
 */
public interface Cacheable<ID> {
    ID cacheId();
}
