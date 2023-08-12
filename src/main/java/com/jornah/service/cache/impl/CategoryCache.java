package com.jornah.service.cache.impl;

import com.jornah.model.entity.Category;
import com.jornah.service.cache.AbstractCacheService;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RBucket;
import org.redisson.api.RList;
import org.springframework.stereotype.Service;

/**
 * @author licong
 * @date 2023/2/15 00:15
 */
@Service
@Slf4j
public class CategoryCache extends AbstractCacheService<Long, Category> {
    private static final String CATEGORY = "CATEGORY";
    private static final String ALL_CATEGORY = "ALL_CATEGORY";

    @Override
    protected RBucket<Category> getBucketByKey(Long id) {
        String key = String.format("%s:%s", CATEGORY, id);
        return this.redissonClient.getBucket(key);
    }

    @Override
    protected RList<Category> getListByKey() {
        return this.redissonClient.getList(ALL_CATEGORY);
    }

    @Override
    protected void hitLog(Long id) {
        if (log.isInfoEnabled()) {
            log.info("hit cache, Category id:{}",id);
        }
    }

}
