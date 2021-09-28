package com.jornah.model.vo;

import com.jornah.model.newP.Article;
import com.jornah.model.newP.Category;
import com.jornah.model.newP.Tag;
import lombok.Data;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Data
@SuperBuilder
public class ArticleVo {
    private Article article;

    // ------- other fields

    private List<Tag> tags;
    private List<Category> categories;

}
