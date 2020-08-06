/**
 * Created by IntelliJ IDEA.
 * User: Jornah Lee
 * DateTime: 2018/7/24 17:03
 **/
package com.wip.dao;

import com.wip.model.dto.MetaDto;
import com.wip.model.dto.cond.MetaCond;
import com.wip.model.Meta;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * 项目相关Dao接口
 */
@Mapper
@Repository
public interface MetaDao {

    /**
     * 添加项目
     * @param meta  项目对象
     * @return
     */
    int addMeta(Meta meta);

    /**
     * 更新项目
     * @param meta  项目对象
     * @return
     */
    int updateMeta(Meta meta);

    /**
     * 根据条件查询
     * @param metaCond
     * @return
     */
    List<Meta> getMetasByCond(MetaCond metaCond);

    /**
     * 根据ID获取项目
     * @param mid
     * @return
     */
    Meta getMetaById(@Param("mid") Integer mid);

    /**
     * 根据sql查询
     * @param parMap
     * @return
     */
    List<MetaDto> selectFromSql(Map<String, Object> parMap);

    /**
     * 删除项目
     * @param mid
     */
    void deleteMetaById(Integer mid);

    /**
     * 根据类型获取meta数量
     * @param type
     * @return
     */
    Long getMetasCountByType(@Param("type") String type);

    /**
     * 通过Meta名查找Meta项目
     * @param type
     * @param name
     * @return
     */
    Meta getMetaByName(@Param("type") String type, @Param("name") String name);
}
