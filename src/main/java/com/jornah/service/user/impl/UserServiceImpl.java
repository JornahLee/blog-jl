/**
 * Created by IntelliJ IDEA.
 * User: Jornah Lee
 * DateTime: 2018/7/23 14:27
 **/
package com.jornah.service.user.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.jornah.cache.CacheService;
import com.jornah.dao.ArticleDao;
import com.jornah.dao.ReadRecordDao;
import com.jornah.dao.UserDao;
import com.jornah.exception.BusinessException;
import com.jornah.model.entity.Article;
import com.jornah.model.entity.User;
import com.jornah.model.qo.ReadRecord;
import com.jornah.service.user.BaseService;
import com.jornah.service.user.UserService;
import com.jornah.utils.WebRequestHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.util.List;
import java.util.Objects;

import static com.jornah.constant.ExceptionType.INVALID_PASSWORD;
import static com.jornah.constant.ExceptionType.TOO_MANY_REQUESTS;

/**
 * 用户相关Service接口实现
 */
@Service
public class UserServiceImpl extends BaseService implements UserService {

    public static final String LOGIN_FAIL_COUNT = "login:fail:count";
    public static final String RECENT_READ = "RecentRead";
    @Autowired
    private UserDao userDao;
    @Autowired
    private CacheService cacheService;
    @Autowired
    private ArticleDao articleDao;
    @Autowired
    private ReadRecordDao readRecordDao;


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
                throw BusinessException.of(TOO_MANY_REQUESTS, "密码错误此处过多");
            }
            throw BusinessException.of(INVALID_PASSWORD, "密码错");
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

    @Override
    public void saveReadRecord(ReadRecord readRecord) {
        Long userId = WebRequestHelper.getCurrentUserInfo().getUserId();
        readRecord.setUserId(userId);
        String title = articleDao.selectOne(new LambdaQueryWrapper<Article>().eq(Article::getId, readRecord.getArticleId())).getTitle();
        readRecord.setTitle(title);
        readRecordDao.insertOrUpdate(readRecord);
    }

    @Override
    public List<ReadRecord> getRecentRead() {
        return readRecordDao.getRecentRead();
    }

}
