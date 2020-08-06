package com.wip.service.es;

import com.wip.dao.ContentDao;
import com.wip.model.Content;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;


@SpringBootTest
class EsContentServiceTest {
    @Autowired
    private EsContentService service;

    @Test
    public void test1(){
        Content content = new Content();
        content.setCid(250);
        content.setContent("这里是博客 this is content");
    }

    @Autowired
    ContentDao contentDao;
    @Test
    public void test2(){
        Content content = new Content();
        content.setCid(250);
        content.setContent("这里是博客 this is content");
        contentDao.addArticle(content);
    }

}