package com.jornah.model.vo;

import com.jornah.model.entity.Category;
import com.jornah.model.entity.Tag;
import lombok.Builder;
import lombok.Data;

import java.util.List;

/**
 * @author licong
 * @date 2021/10/26 00:02
 */
@Data
@Builder
public class ArticleMetaInfo {
    List<Tag> tags;
    Category category;
}
