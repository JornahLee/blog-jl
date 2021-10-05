/**
 * Created by IntelliJ IDEA.
 * User: Administrator
 * DateTime: 2018/7/27 21:55
 **/
package com.jornah.service.option.impl;

import com.jornah.constant.ErrorConstant;
import com.jornah.dao.OptionDao;
import com.jornah.exception.BusinessException;
import com.jornah.model.Options;
import com.jornah.service.option.OptionService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
public class OptionServiceImpl implements OptionService {

    @Autowired
    private OptionDao optionDao;

    @Override

    public List<Options> getOptions() {
        return optionDao.getOptions();
    }

    @Override
    @Transactional

    public void saveOptions(Map<String, String> options) {
        if (null != options && !options.isEmpty()) {
            options.forEach(this::updateOptionByName);
        }
    }

    @Override
    @Transactional

    public void updateOptionByName(String name, String value) {
        if (StringUtils.isBlank(name)) {
            throw BusinessException.of(ErrorConstant.Common.PARAM_IS_EMPTY);
        }
        Options option = new Options();
        option.setName(name);
        option.setValue(value);
        optionDao.updateOptionByName(option);
    }

    @Override

    public Options getOptionByName(String name) {
        if (StringUtils.isBlank(name)) {
            throw BusinessException.of(ErrorConstant.Common.PARAM_IS_EMPTY);
        }
        return optionDao.getOptionByName(name);
    }
}
