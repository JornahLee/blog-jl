package com.jornah.service.cache.impl;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author licong
 * @date 2023/2/15 23:21
 */
@Service
@Getter
public class CacheHolder {
    @Autowired
    private ArticleMetaInfoCache articleMetaInfoCache;
    @Autowired
    private CategoryCache categoryCache;
    @Autowired
    private TagCache tagCache;

}
