/**
 * Created by IntelliJ IDEA.
 * User: Administrator
 * DateTime: 2018/7/27 21:56
 **/
package com.jornah.dao;

import com.jornah.model.Options;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 网站选项相关Dao接口
 */
@Mapper
@Repository
public interface OptionDao {

    /**
     * 获取网站全部信息
     * @return
     */
    List<Options> getOptions();

    /**
     * 更新网站配置
     * @param option
     */
    void updateOptionByName(Options option);

    /**
     * 通过名称网站配置
     * @param name
     * @return
     */
    Options getOptionByName(String name);
}
