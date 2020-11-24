/**
 * Created by IntelliJ IDEA.
 * User: Jornah Lee
 * DateTime: 2018/7/25 16:50
 **/
package com.wip.dao;

import com.wip.model.Content;
import com.wip.model.RelationShip;
import com.wip.model.dto.cond.ContentCond;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 文章相关Dao接口
 */
@Mapper
@Repository
public interface ContentDao {

    /**
     * 添加文章
     * @param content
     */
    void addArticle(Content content);

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
     * @return
     */
    List<Content> getArticleByCond(ContentCond contentCond);

    /**
     * 删除文章
     * @param cid
     */
    void deleteArticleById(Integer cid);

    /**
     * 获取文章总数
     * @return
     */
    Long getArticleCount();

    /**
     * 通过分类名获取文章
     * @param category
     * @return
     */
    List<Content> getArticleByCategory(@Param("category") String category);

    /**
     * 通过标签获取文章
     * @param cid
     * @return
     */
    List<Content> getArticleByTags(List<RelationShip> cid);

    List<Content> findAll();
}
