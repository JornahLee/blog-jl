package com.jornah.service.cache.impl;

import com.jornah.model.vo.ArticleMetaInfo;
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
public class ArticleMetaInfoCache extends AbstractCacheService<Long, ArticleMetaInfo> {
    private static final String ARTICLE_META_INFO = "ARTICLE_META_INFO";

    @Override
    protected RBucket<ArticleMetaInfo> getBucket(Long id) {
        String key = String.format("%s:%s", ARTICLE_META_INFO, id);
        return this.redissonClient.getBucket(key);
    }

    @Override
    protected RList<ArticleMetaInfo> getList() {
        throw new UnsupportedOperationException();
    }

    @Override
    protected void hitLog(Long id) {
        if (log.isInfoEnabled()) {
            log.info("hit cache, metainfo id:{}",id);
        }
    }

}
