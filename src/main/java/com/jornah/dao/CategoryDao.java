package com.jornah.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jornah.model.entity.Category;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author licong
 * @date 2021/9/25 12:03
 */
@Repository
@Mapper
public interface CategoryDao extends BaseMapper<Category> {
    @Select("select c.* from article_category a join category c on a.category_id=c.id where a.article_id=#{arId} limit 1")
    Category findCategoryBy(@Param("arId") Long arId);

    @Insert(" insert article_category(article_id,category_id) values(#{arId},#{cateId})")
    Long insertMap(@Param("arId") Long arId, @Param("cateId") Long cateId);

    @Delete("delete from article_category where article_id=#{arId}")
    void deleteAllMapBy(@Param("arId") Long arId);

    @Select("select article_id from article_category where ca_id=#{caId}")
    List<Long> findArIdsBy(@Param("caId") Long caId);

    @Delete("delete from article_category where article_id=#{arId} and ca_id=#{cateId}")
    void deleteMap(@Param("arId") Long arId, @Param("cateId") Long cateId);

    @Delete("delete from article_category where article_id=#{arId}")
    void deleteMapBy(@Param("arId") Long arId);
}
