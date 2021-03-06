/**
 * Created by IntelliJ IDEA.
 * User: Jornah Lee
 * DateTime: 2018/7/23 14:27
 **/
package com.wip.service.user.impl;

import com.wip.constant.ErrorConstant;
import com.wip.dao.UserDao;
import com.wip.exception.BusinessException;
import com.wip.model.User;
import com.wip.service.user.UserService;
import com.wip.utils.TaleUtils;
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

        if (StringUtils.isBlank(username) || StringUtils.isBlank(password))
            throw BusinessException.withErrorCode(ErrorConstant.Auth.USERNAME_PASSWORD_IS_EMPTY);

        String pwd = TaleUtils.MD5encode(username + password);
        User user = userDao.getUserInfoByCond(username,pwd);
        if (null == user)
            throw BusinessException.withErrorCode(ErrorConstant.Auth.USERNAME_PASSWORD_ERROR);
        return user;
    }

    public static void main(String[] args) {
        String username="admin";
        String password="000000";
        String pwd = TaleUtils.MD5encode(username + password);
        System.out.println("--lcg--- pwd   " + pwd + "     -------");

    }

    @Override
    public User getUserInfoById(Integer uid) {
        return userDao.getUserInfoById(uid);
    }

    // 开启事务
    @Transactional
    @Override
    public int updateUserInfo(User user) {
        if (null == user.getUid())
            throw BusinessException.withErrorCode("用户编号不能为空");
        return userDao.updateUserInfo(user);
    }
}
