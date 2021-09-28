/**
 * Created by IntelliJ IDEA.
 * User: Jornah Lee
 * DateTime: 2018/8/3 16:24
 **/
package com.jornah.service.attach.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.jornah.constant.ErrorConstant;
import com.jornah.dao.AttachDao;
import com.jornah.exception.BusinessException;
import com.jornah.model.Attach;
import com.jornah.model.dto.AttAchDto;
import com.jornah.service.attach.AttAchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 文件接口实现
 */
@Service
public class AttAchServiceImpl implements AttAchService {

    @Autowired
    private AttachDao attAchDao;

    @Override
    public void addAttAch(Attach attach) {
        if (null == attach) {
            throw BusinessException.withErrorCode(ErrorConstant.Common.PARAM_IS_EMPTY);
        }
        attAchDao.addAttAch(attach);
    }

    @Override
    public PageInfo<AttAchDto> getAtts(int pageNum, int pageSize) {
        return PageHelper.startPage(pageNum, pageSize)
                .doSelectPageInfo(()->attAchDao.getAtts());
    }

    @Override
    public AttAchDto getAttAchById(Integer id) {
        if (null == id) {
            throw BusinessException.withErrorCode(ErrorConstant.Common.PARAM_IS_EMPTY);
        }
        return attAchDao.getAttAchById(id);
    }

    @Override

    public void deleteAttAch(Integer id) {
        if (null == id) {
            throw BusinessException.withErrorCode(ErrorConstant.Common.PARAM_IS_EMPTY);
        }
        attAchDao.deleteAttAch(id);
    }
}
