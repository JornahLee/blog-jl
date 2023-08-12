package com.jornah.dao;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jornah.model.vo.SiteInfo;
import com.jornah.utils.APIResponse;
import org.junit.Test;

import java.util.Map;

/**
 * @author licong
 * @date 2023/8/12 00:02
 */
public class TestJackson {
    @Test
    public void serialize() throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        String json="{\"code\":\"success\",\"data\":{\"ownerInfo\":{\"username\":null,\"email\":null,\"homeUrl\":null,\"screenName\":null,\"accessToken\":null,\"projIntroduction\":null,\"introduction\":null,\"avatarUrl\":null,\"signature\":null},\"cateList\":[{\"id\":null,\"created\":null,\"updated\":null,\"name\":\"默认分类\"},{\"id\":null,\"created\":null,\"updated\":null,\"name\":\"Java&JVM\"},{\"id\":null,\"created\":null,\"updated\":null,\"name\":\"DB\"},{\"id\":null,\"created\":null,\"updated\":null,\"name\":\"开发工具\"},{\"id\":null,\"created\":null,\"updated\":null,\"name\":\"框架\"},{\"id\":null,\"created\":null,\"updated\":null,\"name\":\"思想&设计&理论\"},{\"id\":null,\"created\":null,\"updated\":null,\"name\":\"数据结构&算法\"},{\"id\":null,\"created\":null,\"updated\":null,\"name\":\"经验积累\"},{\"id\":null,\"created\":null,\"updated\":null,\"name\":\"其他\"},{\"id\":null,\"created\":null,\"updated\":null,\"name\":\"中间件\"},{\"id\":null,\"created\":null,\"updated\":null,\"name\":\"TODO\"},{\"id\":null,\"created\":null,\"updated\":null,\"name\":\"面试\"},{\"id\":null,\"created\":null,\"updated\":null,\"name\":\"计算机网络\"},{\"id\":null,\"created\":null,\"updated\":null,\"name\":\"问题处理\"},{\"id\":null,\"created\":null,\"updated\":null,\"name\":\"版本&协作\"},{\"id\":null,\"created\":null,\"updated\":null,\"name\":\"运维部署\"},{\"id\":null,\"created\":null,\"updated\":null,\"name\":\"安全相关\"},{\"id\":null,\"created\":null,\"updated\":null,\"name\":\"分布式系统\"},{\"id\":null,\"created\":null,\"updated\":null,\"name\":\"大数据\"}],\"tagList\":[{\"id\":null,\"created\":null,\"updated\":null,\"name\":\"git\"},{\"id\":null,\"created\":null,\"updated\":null,\"name\":\"JVM\"},{\"id\":null,\"created\":null,\"updated\":null,\"name\":\"MySQL\"},{\"id\":null,\"created\":null,\"updated\":null,\"name\":\"优化\"},{\"id\":null,\"created\":null,\"updated\":null,\"name\":\"规范\"},{\"id\":null,\"created\":null,\"updated\":null,\"name\":\"服务器\"},{\"id\":null,\"created\":null,\"updated\":null,\"name\":\"IDEA\"},{\"id\":null,\"created\":null,\"updated\":null,\"name\":\"设计模式\"},{\"id\":null,\"created\":null,\"updated\":null,\"name\":\"排序\"},{\"id\":null,\"created\":null,\"updated\":null,\"name\":\"MyBatis\"},{\"id\":null,\"created\":null,\"updated\":null,\"name\":\"spring\"},{\"id\":null,\"created\":null,\"updated\":null,\"name\":\"leetcode\"},{\"id\":null,\"created\":null,\"updated\":null,\"name\":\"DevOps\"},{\"id\":null,\"created\":null,\"updated\":null,\"name\":\"java\"},{\"id\":null,\"created\":null,\"updated\":null,\"name\":\"Linux\"},{\"id\":null,\"created\":null,\"updated\":null,\"name\":\"lua\"},{\"id\":null,\"created\":null,\"updated\":null,\"name\":\"redis\"}],\"statsInfoList\":[\"本站已累计运行1039天\",\"已发布149篇笔记/博客\",\"所有文章累计翻阅5819次\",\"继续坚持，加油！\\uD83D\\uDE0A\"]},\"msg\":null}";
                APIResponse<SiteInfo> siteInfoAPIResponse = objectMapper.readValue(json, new TypeReference<APIResponse<SiteInfo>>() {
        });

        String jsonStr = objectMapper.writeValueAsString(siteInfoAPIResponse);
        System.out.println(jsonStr);

    }
}
