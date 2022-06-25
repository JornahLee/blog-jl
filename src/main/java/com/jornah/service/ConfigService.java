package com.jornah.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.jornah.dao.ConfigDao;
import com.jornah.model.entity.Config;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author licong
 * @date 2022/6/26 01:18
 */
@Service
public class ConfigService {
    @Autowired
    private ConfigDao configDao;

    public Config getConfigByKey(String key){
        return  configDao.selectByKey(key);
    }

}
