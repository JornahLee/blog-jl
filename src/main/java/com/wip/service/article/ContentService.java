/**
 * Created by IntelliJ IDEA.
 * User: Jornah Lee
 * DateTime: 2018/7/25 16:48
 **/
package com.wip.service.article;

import com.github.pagehelper.PageInfo;
import com.wip.model.Content;
import com.wip.model.Meta;
import com.wip.model.dto.cond.ContentCond;
import com.wip.model.vo.ContentMetaInfo;

import java.util.List;

/**
 * 文章相关Service接口
 */
public interface ContentService {

    /***
     * 添加文章
     * @param content
     */
    long addArticle(Content content);

    /**
     * 根据编号获取文章
     * @param cid
     * @return
     */
    Content getArticleById(Integer cid);

    /**
     * 更新文章
     * @param content
     */
    void updateArticleById(Content content);

    /**
     * 根据条件获取文章列表
     * @param contentCond
     * @param page
     * @param limit
     * @return
     */
    PageInfo<Content> getArticlesByCond(ContentCond contentCond, int page, int limit);

    PageInfo<ContentMetaInfo> getArticleMetaInfos(ContentCond contentCond, int pageNum, int pageSize);

    /**
     * 删除文章
     * @param cid
     */
    void deleteArticleById(Integer cid);

    /**
     * 添加文章点击量
     * @param content
     */
    void updateContentByCid(Content content);

    /**
     * 通过分类获取文章
     * @param category
     * @return
     */
    List<Content> getArticleByCategory(String category);

    /**
     * 通过标签获取文章
     * @param tags
     * @return
     */
    List<Content> getArticleByTags(Meta tags);
}
