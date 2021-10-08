package com.jornah.dao.es;

import com.jornah.model.entity.Article;

import java.util.List;

public interface ContentRepository {
    List<Article> findByContentOrTitle(String content, String title);
}
