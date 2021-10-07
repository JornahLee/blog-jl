/**
 * Created by IntelliJ IDEA.
 * User: Jornah Lee
 * DateTime: 2018/8/3 16:25
 **/
package com.jornah.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jornah.model.Attach;
import com.jornah.model.dto.AttachDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 文件相关Dao接口
 */
@Mapper
@Repository
public interface AttachDao extends BaseMapper<Attach> {

    /**
     * 获取所有的附件信息
     *
     * @return
     */
    @Select("select * from attach")
    List<AttachDto> getAtts();

    @Select("select * from attach where md5=#{md5}")
    Attach findByMd5(@Param("md5") String md5);
}
