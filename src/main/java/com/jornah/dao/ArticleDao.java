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

    @Update("update article set status=#{status} where id =#{id}")
    void updateStatusById(Long id, String status);

    @Update("update article set hits=#{hits} where cid =#{id}")
    void updateHitsById(int id, long hits);

    @Select("select * from article where status='PUBLISH' order by updated")
    List<Article> findArticles();

    @Select("select a.id, a.title, a.created, a.updated, a.author_id, a.hits, a.comments_num, a.allow_comment, a.allow_ping, a.allow_feed, a.order_weight " +
            "from article a join article_category ac on a.id=ac.article_id " +
            "where ac.category_id=#{cateId} and a.status='PUBLISH' order by a.created")
    List<Article> findArticlesByCategory(@Param("cateId") Long cateId);

    //todo 如果的确有需求 可以做一个  通过多个tag过滤出文章的功能
    @Select("select a.id, a.title, a.created, a.updated, a.author_id, a.hits, a.comments_num, a.allow_comment, a.allow_ping, a.allow_feed, a.order_weight " +
            "from article a join article_tag art on a.id=art.article_id " +
            "where art.tag_Id=#{tagId} and a.status='PUBLISH' order by a.created")
    List<Article> findArticlesByTag(@Param("tagId") Long tagId);


    List<Article> findAll();
}
