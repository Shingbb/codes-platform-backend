package com.shing.codesplatformbackend.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.mybatisflex.core.query.QueryWrapper;
import com.mybatisflex.spring.service.impl.ServiceImpl;
import com.shing.codesplatformbackend.exception.BusinessException;
import com.shing.codesplatformbackend.exception.ErrorCode;
import com.shing.codesplatformbackend.mapper.UserMapper;
import com.shing.codesplatformbackend.model.entity.User;
import com.shing.codesplatformbackend.model.enums.UserRoleEnum;
import com.shing.codesplatformbackend.model.vo.LoginUserVO;
import com.shing.codesplatformbackend.service.UserService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.nio.charset.StandardCharsets;

import static com.shing.codesplatformbackend.exception.ErrorCode.*;

/**
 * 用户服务实现类
 */
@Service
@Slf4j
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    /**
     * Spring Security 的 BCrypt 加密算法
     */
    @Resource
    private BCryptPasswordEncoder passwordEncoder;

    /**
     * 用户注册
     *
     * @param userAccount   用户账号
     * @param userPassword  用户密码
     * @param checkPassword 确认密码
     * @return 用户id
     */
    @Override
    public long userRegister(String userAccount, String userPassword, String checkPassword) {
        // 1. 校验
        if (StrUtil.hasBlank(userAccount, userPassword, checkPassword)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "参数为空");
        }
        if (userAccount.length() < 4) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "用户账号过短");
        }
        if (userPassword.length() < 8 || checkPassword.length() < 8) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "用户密码过短");
        }
        if (!userPassword.equals(checkPassword)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "两次输入的密码不一致");
        }
        // 2. 检查是否重复
        QueryWrapper queryWrapper = QueryWrapper.create();
        queryWrapper.eq(User::getUserAccount, userAccount);
        long count = this.mapper.selectCountByQuery(queryWrapper);
        if (count > 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "账号重复");
        }

        // 3. 加密
        String encryptPassword = getEncryptPassword(userPassword);

        // 4. 插入数据
        User user = new User();
        user.setUserAccount(userAccount);
        user.setUserPassword(encryptPassword);
        user.setUserName("无名");
        user.setUserRole(UserRoleEnum.USER.getValue());
        boolean saveResult = this.save(user);
        if (!saveResult) {
            throw new BusinessException(SYSTEM_ERROR, "注册失败，数据库错误");
        }
        return user.getId();
    }

    /**
     * 用户登录
     * @param userAccount  用户账户
     * @param userPassword 用户密码
     * @return 登录用户信息
     */
    @Override
    public LoginUserVO userLogin(String userAccount, String userPassword) {
        // 1. 校验
        if (StrUtil.hasBlank(userAccount, userPassword)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "参数为空");
        }
        if (userAccount.length() < 4) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "账号错误");
        }
        if (userPassword.length() < 8) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "密码错误");
        }
        // 2.查询用户是否存在
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("userAccount", userAccount);
        User user = this.mapper.selectOneByQuery(queryWrapper);

        if (user == null || !passwordEncoder.matches(userPassword, user.getUserPassword())) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "用户不存在或密码错误");
        }
        // 4. 登录成功，记录用户的登录态
        StpUtil.login(user.getId());
        StpUtil.getSession().set("user_login", user);
        // 5. 获得脱敏后的用户信息
        return this.getLoginUserVO(user);
    }

    /**
     * 获取脱敏后的用户信息
     * @param user  用户
     * @return 脱敏后的用户信息
     */
    @Override
    public LoginUserVO getLoginUserVO(User user) {
        if (user == null) {
            return null;
        }
        LoginUserVO loginUserVO = new LoginUserVO();
        BeanUtil.copyProperties(user, loginUserVO);
        return loginUserVO;
    }

    /**
     * 获取当前登录用户
     * @return 当前登录用户
     */
    @Override
    public User getLoginUser() {
        // 先判断是否已登录
        if (!StpUtil.isLogin()) {
            throw new BusinessException(ErrorCode.NOT_LOGIN_ERROR, "未登录");
        }
        Object userObj = StpUtil.getSession().get("user_login");
        User currentUser = (User) userObj;
        if (currentUser == null || currentUser.getId() == null) {
            throw new BusinessException(ErrorCode.NOT_LOGIN_ERROR,"未登录");
        }
        // 从数据库查询（追求性能的话可以注释，直接返回上述结果）
        long userId = currentUser.getId();
        currentUser = this.getById(userId);
        if (currentUser == null) {
            throw new BusinessException(ErrorCode.NOT_LOGIN_ERROR,"未登录");
        }
        return currentUser;
    }

    /**
     * 用户注销
     * @return 是否注销成功
     */
    @Override
    public boolean userLogout() {
        if (!StpUtil.isLogin() || StpUtil.getSession().get("user_login") == null) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "未登录");
        }
        // 移除登录态
        StpUtil.logout();
        return true;
    }

    /**
     * 对明文密码进行加密处理（使用 BCrypt）
     *
     * @param userPassword 用户密码
     * @return 加密后的密码
     */
    private String getEncryptPassword(String userPassword) {
        // 使用BCryptPasswordEncoder进行密码加密
        return passwordEncoder.encode(userPassword);
    }

    /**
     * 对明文密码进行简单加密处理（使用 MD5）
     * @param userPassword 明文密码
     * @return 密文密码
     */
    public String getSaltEncryptPassword(String userPassword) {
        // 盐值，混淆密码
        final String SALT = "shing";
        return DigestUtils.md5DigestAsHex((userPassword + SALT).getBytes(StandardCharsets.UTF_8));
    }
}