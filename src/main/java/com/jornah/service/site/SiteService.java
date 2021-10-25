/**
 * Created by IntelliJ IDEA.
 * User: Jornah Lee
 * DateTime: 2018/8/2 8:48
 **/
package com.jornah.service.site;

import com.jornah.model.entity.Comment;
import com.jornah.model.entity.Article;
import com.jornah.model.dto.StatisticsDto;

import java.util.List;

/**
 * 网站相关Service接口
 */
public interface SiteService {

    /**
     * 获取评论列表
     * @param limit
     * @return
     */
    List<Comment> getComments(int limit);

    /**
     * 获取文章列表
     * @param limit
     * @return
     */
    List<Article> getNewArticles(int limit);

    /**
     * 获取后台统计数
     * @return
     */
    StatisticsDto getStatistics();
}
