/**
 * Created by IntelliJ IDEA.
 * User: Jornah Lee
 * DateTime: 2018/7/23 14:27
 **/
package com.jornah.service.user.impl;

import com.jornah.cache.CacheService;
import com.jornah.dao.UserDao;
import com.jornah.exception.BusinessException;
import com.jornah.model.newP.User;
import com.jornah.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.util.Objects;

/**
 * 用户相关Service接口实现
 */
@Service
public class UserServiceImpl implements UserService {

    public static final String LOGIN_FAIL_COUNT = "login:fail:count";
    @Autowired
    private UserDao userDao;//这里会报错，但是并不影响
    @Autowired
    private CacheService cacheService;//这里会报错，但是并不影响


    @Override
    public User login(String username, String password) {
        //todo 先暂时用用明文 后期优化再使用BCryptPasswordEncoder
        User user = userDao.findByUsernameAndPassword(username, password);
        checkLoginCount(user);
        return user;
    }

    private void checkLoginCount(User user) {
        if (Objects.isNull(user)) {
            cacheService.setValueIfAbsent(LOGIN_FAIL_COUNT, String.valueOf(1), Duration.ofSeconds(60));
            if (cacheService.increment(LOGIN_FAIL_COUNT) > 3) {
                throw BusinessException.of("密码错误此处过多");
            }
            throw BusinessException.of("密码错");
        }
    }

    @Override
    public User getById(Long uid) {
        return userDao.selectById(uid);
    }

    // 开启事务
    @Transactional
    @Override
    public int updateUserInfo(User user) {
//        if (null == user.getUid()) {
//            throw BusinessException.withErrorCode("用户编号不能为空");
//        }
        return 0;
    }
}
