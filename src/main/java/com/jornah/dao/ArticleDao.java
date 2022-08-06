/**
 * Created by IntelliJ IDEA.
 * User: Jornah Lee
 * DateTime: 2018/7/25 16:50
 **/
package com.jornah.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jornah.model.entity.Article;
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

    @Select("select * from article where status='PUBLISH' and recommend_level > 0  order by recommend_level desc , updated desc limit #{size}")
    List<Article> findByRecommend(@Param("size") int size);

    List<Article> findArticlesByCategory(@Param("cateId") Long cateId, @Param("statusList") List<String> statusList);

    List<Article> findArticlesByTag(@Param("tagId") Long tagId, @Param("statusList") List<String> statusList);

    List<Article> findArticlesByStatus( @Param("statusList") List<String> statusList);

    @Select("select * from article")
    List<Article> findAll();

    @Select("select a.id from article a where a.status='PUBLISH' order by a.created asc")
    List<Long> findAllIdBy();
}
