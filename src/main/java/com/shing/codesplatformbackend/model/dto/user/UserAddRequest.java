package com.shing.codesplatformbackend.model.dto.user;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * 用户创建请求
 *
 * @author Shing
 * date 4/8/2025
 */
@Data
public class UserAddRequest implements Serializable {

    /**
     * 用户昵称
     */
    @NotBlank(message = "昵称不能为空", groups = ValidationGroups.Create.class)
    @Size(min = 2, max = 20, message = "昵称长度应为2-20位", groups = ValidationGroups.Create.class)
    private String userName;

    /**
     * 账号
     */
    @NotBlank(message = "账号不能为空", groups = ValidationGroups.Create.class)
    @Size(min = 4, max = 20, message = "账号长度应为4-20位", groups = ValidationGroups.Create.class)
    private String userAccount;

    /**
     * 用户头像
     */
    @Size(max = 255, message = "头像地址过长", groups = ValidationGroups.Create.class)
    private String userAvatar;

    /**
     * 用户简介
     */
    @Size(max = 500, message = "简介长度不能超过500字符", groups = ValidationGroups.Create.class)
    private String userProfile;

    /**
     * 用户角色: user, admin
     */
    @Pattern(regexp = "user|admin", message = "用户角色只能是 user 或 admin", groups = ValidationGroups.Create.class)
    private String userRole;

    @Serial
    private static final long serialVersionUID = 1L;
}
