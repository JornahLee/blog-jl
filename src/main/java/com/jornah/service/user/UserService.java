/**
 * Created by IntelliJ IDEA.
 * User: Jornah Lee
 * DateTime: 2018/7/23 14:26
 **/
package com.jornah.service.user;

import com.jornah.model.entity.User;
import com.jornah.model.qo.ReadRecord;

import java.util.List;

/**
 * 用户相关Service接口
 */
public interface UserService {

    /**
     * 用户登录
     * @param username  用户名
     * @param password  密码
     * @return
     */
    User login(String username, String password);

    /**
     * 通过用户ID获取用户信息
     * @param uid   主键
     * @return
     */
    User getById(Long id);

    /**
     * 更改用户信息
     * @param user  user对象
     * @return
     */
    int updateUserInfo(User user);

    void saveReadRecord(ReadRecord readRecord);

    List<ReadRecord> getRecentRead();
}
