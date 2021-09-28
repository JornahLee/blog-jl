/**
 * Created by IntelliJ IDEA.
 * User: Jornah Lee
 * DateTime: 2018/7/23 16:59
 **/
package com.jornah.service.log.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.jornah.dao.LogDao;
import com.jornah.model.Log;
import com.jornah.service.log.LogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 日志相关Service接口实现
 */
@Service
public class LogServiceImpl implements LogService {


    @Autowired
    private LogDao logDao;


    @Override
    public void addLog(String action, String data, String ip, Integer authorId) {
        Log log = new Log();
        log.setAuthorId(authorId);
        log.setIp(ip);
        log.setData(data);
        log.setAction(action);
        logDao.addLog(log);
    }

    @Override
    public PageInfo<Log> getLogs(int pageNum, int pageSize) {
        return PageHelper.startPage(pageNum,pageSize)
                .doSelectPageInfo(()->logDao.getLogs());

    }
}
