/**
 * Created by IntelliJ IDEA.
 * User: Jornah Lee
 * DateTime: 2018/7/23 14:27
 **/
package com.jornah.service.user.impl;

import com.jornah.constant.ErrorConstant;
import com.jornah.dao.UserDao;
import com.jornah.exception.BusinessException;
import com.jornah.model.newP.User;
import com.jornah.service.user.UserService;
import com.jornah.utils.TaleUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 用户相关Service接口实现
 */
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDao userDao;//这里会报错，但是并不影响


    @Override
    public User login(String username, String password) {

        if (StringUtils.isBlank(username) || StringUtils.isBlank(password)) {
            throw BusinessException.withErrorCode(ErrorConstant.Auth.USERNAME_PASSWORD_IS_EMPTY);
        }

        String pwd = TaleUtils.MD5encode(username + password);
        User user = userDao.getUserInfoByCond(username,pwd);
        if (null == user) {
            throw BusinessException.withErrorCode(ErrorConstant.Auth.USERNAME_PASSWORD_ERROR);
        }
        return user;
    }

    @Override
    public User getUserInfoById(Integer uid) {
        return userDao.getUserInfoById(uid);
    }

    // 开启事务
    @Transactional
    @Override
    public int updateUserInfo(User user) {
//        if (null == user.getUid()) {
//            throw BusinessException.withErrorCode("用户编号不能为空");
//        }
        return userDao.updateUserInfo(user);
    }
}
