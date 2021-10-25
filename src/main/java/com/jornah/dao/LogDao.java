/**
 * Created by IntelliJ IDEA.
 * User: Jornah Lee
 * DateTime: 2018/7/23 17:07
 **/
package com.jornah.dao;


import com.jornah.model.entity.Log;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 日志dao层接口
 */
@Mapper
@Repository
public interface LogDao {

    /**
     * 添加日志
     * @param log
     * @return
     */
    int addLog(Log log);

    /**
     * 获取日志
     * @return
     */
    List<Log> getLogs();
}
