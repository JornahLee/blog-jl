/**
 * Created by IntelliJ IDEA.
 * User: Jornah Lee
 * DateTime: 2018/7/23 17:07
 **/
package com.jornah.dao;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jornah.model.Draft;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface DraftDao extends BaseMapper<Draft> {

}
