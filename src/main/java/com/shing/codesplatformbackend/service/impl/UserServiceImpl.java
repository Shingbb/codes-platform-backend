package com.shing.codesplatformbackend.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import cn.hutool.core.text.CharSequenceUtil;
import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.query.QueryWrapper;
import com.mybatisflex.spring.service.impl.ServiceImpl;
import com.shing.codesplatformbackend.exception.BusinessException;
import com.shing.codesplatformbackend.exception.ErrorCode;
import com.shing.codesplatformbackend.mapper.UserMapper;
import com.shing.codesplatformbackend.model.dto.user.*;
import com.shing.codesplatformbackend.model.entity.User;
import com.shing.codesplatformbackend.model.enums.UserRoleEnum;
import com.shing.codesplatformbackend.model.vo.LoginUserVO;
import com.shing.codesplatformbackend.model.vo.UserVO;
import com.shing.codesplatformbackend.service.UserService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;

import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.List;

import static com.shing.codesplatformbackend.exception.ErrorCode.SYSTEM_ERROR;

/**
 * 用户服务实现类
 */
@Service
@Slf4j
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    private static final String SESSION_USER_LOGIN = "user_login";

    private static final String ERROR_USER_NOT_LOGIN = "用户未登录";
    /**
     * Spring Security 的 BCrypt 加密算法
     */
    @Resource
    private BCryptPasswordEncoder passwordEncoder;

    /**
     * 用户注册
     *
     * @param userRegisterRequest 用户注册信息
     * @return 用户id
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public long userRegister(UserRegisterRequest userRegisterRequest) {
        String userAccount = userRegisterRequest.getUserAccount();
        String userPassword = userRegisterRequest.getUserPassword();
        String checkPassword = userRegisterRequest.getCheckPassword();
        // 1. 校验
        if (!userPassword.equals(checkPassword)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "两次密码不一致");
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
     *
     * @param userLoginRequest 用户登录信息
     * @return 登录用户信息
     */
    @Override
    public LoginUserVO userLogin(UserLoginRequest userLoginRequest) {
        String userAccount = userLoginRequest.getUserAccount();
        String userPassword = userLoginRequest.getUserPassword();

        // 1. 校验
        if (CharSequenceUtil.hasBlank(userAccount, userPassword)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "参数为空");
        }
        if (userAccount.length() < 4) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "账号错误");
        }
        if (userPassword.length() < 8) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "密码错误");
        }
        // 2.查询用户是否存在
        QueryWrapper queryWrapper = QueryWrapper.create().eq("userAccount", userAccount);
        User user = this.mapper.selectOneByQuery(queryWrapper);

        if (user == null || !passwordEncoder.matches(userPassword, user.getUserPassword())) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "用户不存在或密码错误");
        }
        // 4. 登录成功，记录用户的登录态
        StpUtil.login(user.getId());
        StpUtil.getSession().set(SESSION_USER_LOGIN, user);
        // 5. 获得脱敏后的用户信息
        return getLoginUserVO(user);
    }

    /**
     * 获取脱敏后的用户信息
     *
     * @param user 用户
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
     *
     * @return 当前登录用户
     */
    @Override
    public User getLoginUser() {
        // 先判断是否已登录
        if (!StpUtil.isLogin()) {
            throw new BusinessException(ErrorCode.NOT_LOGIN_ERROR, ERROR_USER_NOT_LOGIN);
        }
        Object userObj = StpUtil.getSession().get(SESSION_USER_LOGIN);
        User currentUser = (User) userObj;
        if (currentUser == null || currentUser.getId() == null) {
            throw new BusinessException(ErrorCode.NOT_LOGIN_ERROR, ERROR_USER_NOT_LOGIN);
        }
        // 从数据库查询（追求性能的话可以注释，直接返回上述结果）
        long userId = currentUser.getId();
        currentUser = this.getById(userId);
        if (currentUser == null) {
            throw new BusinessException(ErrorCode.NOT_LOGIN_ERROR, ERROR_USER_NOT_LOGIN);
        }
        return currentUser;
    }

    @Override
    public UserVO getUserVO(User user) {
        if (user == null) {
            return null;
        }
        UserVO userVO = new UserVO();
        BeanUtil.copyProperties(user, userVO);
        return userVO;
    }

    @Override
    public List<UserVO> getUserVOList(List<User> records) {
        if (records == null || records.isEmpty()) {
            return Collections.emptyList();
        }
        return records.stream().map(this::getUserVO).toList();
    }

    @Override
    public Long addUser(UserAddRequest userAddRequest) {
        User user = new User();
        BeanUtil.copyProperties(userAddRequest, user);
        String defaultPassword = "12345678";
        user.setUserPassword(getEncryptPassword(defaultPassword));
        if (!save(user)) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "添加用户失败");
        }
        return user.getId();
    }

    @Override
    public Boolean updateUser(UserUpdateRequest userUpdateRequest) {
        User user = new User();
        BeanUtil.copyProperties(userUpdateRequest, user, CopyOptions.create().ignoreNullValue());
        return updateById(user);
    }

    @Override
    public Boolean deleteUserById(Long id) {
        if (id == null || id <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "无效的用户ID");
        }
        return removeById(id);
    }

    /**
     * 用户注销
     *
     * @return 是否注销成功
     */
    @Override
    public boolean userLogout() {
        if (!StpUtil.isLogin() || StpUtil.getSession().get(SESSION_USER_LOGIN) == null) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, ERROR_USER_NOT_LOGIN);
        }
        // 移除登录态
        StpUtil.logout();
        return true;
    }

    @Override
    public QueryWrapper getQueryWrapper(UserQueryRequest userQueryRequest) {
        if (userQueryRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "请求参数为空");
        }
        Long id = userQueryRequest.getId();
        String userAccount = userQueryRequest.getUserAccount();
        String userName = userQueryRequest.getUserName();
        String userProfile = userQueryRequest.getUserProfile();
        String userRole = userQueryRequest.getUserRole();
        String sortField = userQueryRequest.getSortField();
        String sortOrder = userQueryRequest.getSortOrder();
        return QueryWrapper.create()
                .eq("id", id) // where id =${id}
                .eq("userRole", userRole) // and userRole = ${userRole}
                .like("userAccount", userAccount)
                .like("userName", userName)
                .like("userProfile", userProfile)
                .orderBy(sortField, "ascend".equals(sortOrder));
    }

    @Override
    public Page<UserVO> listUserVOByPage(UserQueryRequest userQueryRequest) {
        // 从请求中获取分页参数
        long pageNum = userQueryRequest.getPageNum();
        long pageSize = userQueryRequest.getPageSize();

        // 调用 MyBatis-Flex 分页查询，传入分页对象和条件构造器
        Page<User> userPage = this.page(Page.of(pageNum, pageSize), getQueryWrapper(userQueryRequest));

        // 将查询出的 User 实体列表转换成 UserVO 列表
        List<UserVO> userVOList = getUserVOList(userPage.getRecords());

        // 新建一个 UserVO 类型的分页对象，传入页码、页大小和总记录数
        Page<UserVO> userVOPage = new Page<>(pageNum, pageSize, userPage.getTotalRow());

        // 设置分页数据记录
        userVOPage.setRecords(userVOList);

        return userVOPage;
    }


    /**
     * 对明文密码进行加密处理（使用 BCrypt）
     *
     * @param userPassword 用户密码
     * @return 加密后的密码
     */
    @Override
    public String getEncryptPassword(String userPassword) {
        // 使用BCryptPasswordEncoder进行密码加密
        return passwordEncoder.encode(userPassword);
    }

    /**
     * 对明文密码进行简单加密处理（使用 MD5）
     *
     * @param userPassword 明文密码
     * @return 密文密码
     */
    public String getSaltEncryptPassword(String userPassword) {
        // 盐值，混淆密码
        final String SALT = "shing";
        return DigestUtils.md5DigestAsHex((userPassword + SALT).getBytes(StandardCharsets.UTF_8));
    }
}