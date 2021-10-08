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
import com.jornah.model.entity.Attach;
import com.jornah.model.converter.AttachConverter;
import com.jornah.model.dto.AttachDto;
import com.jornah.service.attach.AttachService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 文件接口实现
 */
@Service
public class AttachServiceImpl implements AttachService {

    @Autowired
    private AttachDao attAchDao;

    @Override
    public void addAttach(Attach attach) {
        attAchDao.insert(attach);
    }

    @Override
    public PageInfo<AttachDto> getAtts(int pageNum, int pageSize) {
        return PageHelper.startPage(pageNum, pageSize)
                .doSelectPageInfo(()->attAchDao.getAtts());
    }

    @Override
    public AttachDto getAttachById(Integer id) {
        Attach attach = attAchDao.selectById(id);
        return AttachConverter.INSTANCE.toDto(attach);
    }

    @Override

    public void deleteAttach(Integer id) {
        if (null == id) {
            throw BusinessException.of(ErrorConstant.Common.PARAM_IS_EMPTY);
        }
        attAchDao.deleteById(id);
    }
}
