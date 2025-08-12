package com.shing.codesplatformbackend.model.dto.user;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * 用户更新请求
 */
@Data
public class UserUpdateRequest implements Serializable {

    /**
     * id
     */
    @NotNull(message = "用户 ID 不能为空", groups = ValidationGroups.Update.class)
    private Long id;

    /**
     * 用户昵称
     */
    @Size(min = 2, max = 20, message = "昵称长度应为2-20位", groups = ValidationGroups.Update.class)
    private String userName;

    /**
     * 用户头像
     */
    @Size(max = 255, message = "头像地址过长", groups = ValidationGroups.Update.class)
    private String userAvatar;

    /**
     * 简介
     */
    @Size(max = 500, message = "简介长度不能超过500字符", groups = ValidationGroups.Update.class)
    private String userProfile;

    /**
     * 用户角色：user/admin/ban
     */
    private String userRole;

    @Serial
    private static final long serialVersionUID = 1L;
}