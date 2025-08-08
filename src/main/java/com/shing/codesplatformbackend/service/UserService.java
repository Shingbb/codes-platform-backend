package com.shing.codesplatformbackend.service;

import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.query.QueryWrapper;
import com.mybatisflex.core.service.IService;
import com.shing.codesplatformbackend.model.dto.user.*;
import com.shing.codesplatformbackend.model.entity.User;
import com.shing.codesplatformbackend.model.vo.LoginUserVO;
import com.shing.codesplatformbackend.model.vo.UserVO;

import java.util.List;

/**
 * 用户 服务层。
 *
 * @author <a href="https://github.com/shingbb">程序员shing</a>
 */
public interface UserService extends IService<User> {

    // --- 用户注册、登录相关 ---

    /**
     * 用户注册
     */
    long userRegister(UserRegisterRequest userRegisterRequest);

    /**
     * 用户登录
     */
    LoginUserVO userLogin(UserLoginRequest userLoginRequest);

    /**
     * 用户注销
     */
    boolean userLogout();

    /**
     * 获取当前登录用户
     */
    User getLoginUser();

    /**
     * 获取脱敏的已登录用户信息
     */
    LoginUserVO getLoginUserVO(User user);

    // --- 用户信息增删改相关 ---

    /**
     * 新增用户
     */
    Long addUser(UserAddRequest userAddRequest);

    /**
     * 更新用户信息
     */
    Boolean updateUser(UserUpdateRequest userUpdateRequest);

    /**
     * 根据id删除用户
     */
    Boolean deleteUserById(Long id);

    // --- 查询相关 ---

    /**
     * 根据查询条件构造查询参数
     */
    QueryWrapper getQueryWrapper(UserQueryRequest userQueryRequest);

    /**
     * 分页查询用户信息
     */
    Page<UserVO> listUserVOByPage(UserQueryRequest userQueryRequest);

    /**
     * 将实体转换为视图对象
     */
    UserVO getUserVO(User user);

    /**
     * 将实体列表转换为视图对象列表
     */
    List<UserVO> getUserVOList(List<User> records);

    // --- 密码相关 ---

    /**
     * 对密码进行加密
     */
    String getEncryptPassword(String userPassword);

}
