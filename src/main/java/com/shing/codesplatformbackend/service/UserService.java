package com.shing.codesplatformbackend.service;

import com.mybatisflex.core.service.IService;
import com.shing.codesplatformbackend.model.entity.User;
import com.shing.codesplatformbackend.model.vo.LoginUserVO;

/**
 * 用户 服务层。
 *
 * @author <a href="https://github.com/shingbb">程序员shing</a>
 */
public interface UserService extends IService<User> {

    /**
     * 用户注册
     *
     * @param userAccount   用户账户
     * @param userPassword  用户密码
     * @param checkPassword 确认密码
     * @return 用户id
     */
    long userRegister(String userAccount, String userPassword, String checkPassword);

    /**
     * 用户登录
     *
     * @param userAccount  用户账户
     * @param userPassword 用户密码
     * @return 脱敏后的用户信息
     */
    LoginUserVO userLogin(String userAccount, String userPassword);

    /**
     * 获取脱敏的已登录用户信息
     *
     * @return 脱敏后的用户信息
     */
    LoginUserVO getLoginUserVO(User user);

    /**
     * 获取当前登录用户
     */
    User getLoginUser();

    /**
     * 用户注销
     * @return 用户注销
     */
    boolean  userLogout();

}
