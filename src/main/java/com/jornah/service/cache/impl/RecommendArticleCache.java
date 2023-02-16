package com.jornah.service.cache.impl;

import com.jornah.model.entity.Tag;
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
public class RecommendArticleCache extends AbstractCacheService<Long, Tag> {
    private static final String TAG = "ARTICLE";
    private static final String ALL_TAG = "ALL_TAG";

    @Override
    protected RBucket<Tag> getBucket(Long id) {
        String key = String.format("%s:%s", TAG, id);
        return this.redissonClient.getBucket(key);
    }

    @Override
    protected RList<Tag> getList() {
        return this.redissonClient.getList(ALL_TAG);
    }

    @Override
    protected void hitLog(Long id) {
        if (log.isInfoEnabled()) {
            log.info("hit cache, Tag id:{}",id);
        }
    }

}
