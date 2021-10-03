/**
 * Created by IntelliJ IDEA.
 * User: Jornah Lee
 * DateTime: 2018/8/3 16:25
 **/
package com.jornah.dao;

import com.jornah.model.Attach;
import com.jornah.model.dto.AttAchDto;
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
public interface AttachDao {

    /**
     * 添加单个附件文件
     *
     * @param attach
     */
    @Select("")
    void addAttAch(Attach attach);

    /**
     * 获取所有的附件信息
     *
     * @return
     */
    @Select("")
    List<AttAchDto> getAtts();

    /**
     * 获取附件总数
     *
     * @return
     */
    @Select("")
    Long getAttAchCount();

    /**
     * 通过ID获取附件信息
     *
     * @param id
     * @return
     */
    @Select("")
    AttAchDto getAttAchById(@Param("id") Integer id);

    /**
     * 通过ID删除附件信息
     *
     * @param id
     */
    @Select("")
    void deleteAttAch(@Param("id") Integer id);

    @Select("")
    Attach findByMd5(@Param("md5") String md5);
}
