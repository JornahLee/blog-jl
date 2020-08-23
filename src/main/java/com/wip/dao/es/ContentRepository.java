package com.wip.dao.es;

import com.wip.model.Content;

import java.util.List;

public interface ContentRepository {
    List<Content> findByContentOrTitle(String content, String title);
}
