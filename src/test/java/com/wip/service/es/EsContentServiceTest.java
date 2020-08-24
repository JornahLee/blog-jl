package com.wip.service.es;

import com.google.gson.GsonBuilder;
import com.wip.dao.ContentDao;
import com.wip.model.Content;
import com.wip.model.dto.ContentEsDTO;
import com.wip.model.dto.SearchResult;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.elasticsearch.core.SearchHits;

import java.time.Instant;
import java.util.List;


@SpringBootTest
@Disabled
class EsContentServiceTest {
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
    void testEs() {
        ContentEsDTO obj=new ContentEsDTO();
        obj.setContent("this is content");
        obj.setUrl("sss/wsfsdf");
        obj.setCreated(Instant.now().toEpochMilli());
        obj.setTitle("this is title");
        service.add(obj);
    }

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