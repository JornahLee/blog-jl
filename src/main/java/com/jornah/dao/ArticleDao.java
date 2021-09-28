/**
 * Created by IntelliJ IDEA.
 * User: Jornah Lee
 * DateTime: 2018/7/25 16:50
 **/
package com.jornah.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jornah.model.newP.Article;
import com.jornah.model.RelationShip;
import com.jornah.model.qo.ArticleQo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 文章相关Dao接口
 */
@Mapper
@Repository
public interface ArticleDao extends BaseMapper<Article> {

    /**
     * 添加文章
     *
     * @param article
     */
    long addArticle(Article article);

    @Update("update article set status=#{status} where id =#{id}")
    void updateStatusById(Long id, String status);

    @Update("update t_contents set hits=#{hits} where cid =#{id}")
    void updateHitsById(int id, long hits);


    /**
     * 删除文章
     *
     * @param cid
     */
    void deleteArticleById(Integer cid);

    /**
     * 获取文章总数
     *
     * @return
     */
    Long getArticleCount();

    @Select("select a.* from article a join category c on a.id=c.ar_id where c.cateId=#{cateId}")
    List<Article> getArticleByCategory(@Param("cateId") Long cateId);

    /**
     * 通过标签获取文章
     *
     * @param cid
     * @return
     */
    List<Article> getArticleByTags(List<RelationShip> cid);

    List<Article> findAll();
}
