package com.shing.codesplatformbackend.model.dto.user;

import com.shing.codesplatformbackend.common.PageRequest;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;
import java.io.Serializable;

/**
 * 用户查询请求
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class UserQueryRequest extends PageRequest implements Serializable {

    /**
     * id
     */
    private Long id;

    /**
     * 用户昵称
     */
    @Size(max = 20, message = "昵称长度不能超过20位", groups = ValidationGroups.Query.class)
    private String userName;

    /**
     * 账号
     */
    @Size(max = 20, message = "账号长度不能超过20位", groups = ValidationGroups.Query.class)
    private String userAccount;

    /**
     * 简介
     */
    @Size(max = 500, message = "简介长度不能超过500字符", groups = ValidationGroups.Query.class)
    private String userProfile;

    /**
     * 用户角色：user/admin/ban
     */
    private String userRole;

    @Serial
    private static final long serialVersionUID = 1L;
}