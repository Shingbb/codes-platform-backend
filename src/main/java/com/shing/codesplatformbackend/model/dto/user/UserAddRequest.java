package com.shing.codesplatformbackend.model.dto.user;

/**
 * 用户创建请求
 *
 * @author Shing
 * date 4/8/2025
 */
public class UserAddRequest {

    /**
     * 用户昵称
     */
    private String userName;

    /**
     * 账号
     */
    private String userAccount;

    /**
     * 用户头像
     */
    private String userAvatar;

    /**
     * 用户简介
     */
    private String userProfile;

    /**
     * 用户角色: user, admin
     */
    private String userRole;

    private static final long serialVersionUID = 1L;
}
