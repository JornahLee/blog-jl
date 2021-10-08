package com.jornah.service.es;

import com.google.gson.GsonBuilder;
import com.jornah.dao.ArticleDao;
import com.jornah.model.entity.Article;
import com.jornah.model.dto.SearchResult;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;


@SpringBootTest
@Disabled
class EsArticleServiceTest {
    @Autowired
    private EsContentService service;

    @Test
    void testExportData() {
        service.exportDataToEs();
    }

    @Test
    void testFind() {
        List<SearchResult> res = service.findByContentOrTitle("标题", 1, 1);
        String s = new GsonBuilder().create().toJson(res);
        System.out.println("--licg---     s : " + s + "    -----");
    }

    @Test
    void testUpdate() {
        Article article = new Article();
        article.setId(40L);
        article.setTitle("1235123");
        service.update(article);
    }
    @Test
    void testDelete() {
        service.delete(40+"");
    }

    @Test
    public void test1() {
        Article article = new Article();
        article.setId(250L);
        article.setContent("这里是博客 this is content");
    }

    @Autowired
    ArticleDao articleDao;

    @Test
    public void test2() {
        Article article = new Article();
        article.setId(250L);
        article.setContent("这里是博客 this is content");
        articleDao.insert(article);
    }

}
