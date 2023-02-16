package com.jornah.model.vo;

import com.jornah.model.entity.Category;
import com.jornah.model.entity.Tag;
import com.jornah.service.cache.Cacheable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * @author licong
 * @date 2021/10/26 00:02
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ArticleMetaInfo implements Cacheable<Long>, Serializable {
    Long articleId;
    List<Tag> tags;
    Category category;

    @Override
    public Long getCacheId() {
        return this.articleId;
    }
}
