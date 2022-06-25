package com.jornah.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jornah.model.entity.Article;
import com.jornah.model.entity.Config;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.stereotype.Repository;

/**
 * @author licong
 * @date 2022/6/26 01:48
 */
@Mapper
@Repository
public interface ConfigDao extends BaseMapper<Config> {

    @Select("select * from config where config_key=#{key}")
    Config selectByKey(@Param("key") String key);
}
