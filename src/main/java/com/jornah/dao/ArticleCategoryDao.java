package com.jornah.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jornah.model.entity.ArticleCategoryMap;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * @author licong
 * @date 2021/9/25 12:03
 */
@Repository
@Mapper
public interface ArticleCategoryDao extends BaseMapper<ArticleCategoryMap> {
}
