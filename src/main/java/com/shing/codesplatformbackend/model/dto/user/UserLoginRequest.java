package com.shing.codesplatformbackend.model.dto.user;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * 用户登录请求
 *
 * @author Shing
 * date 5/8/2025
 */
@Data
public class UserLoginRequest implements Serializable {

    /**
     * 账号
     */
    @NotBlank(message = "账号不能为空", groups = ValidationGroups.Login.class)
    @Size(min = 4, max = 20, message = "账号长度应为4-20位", groups = ValidationGroups.Login.class)
    private String userAccount;

    /**
     * 密码
     */
    @NotBlank(message = "密码不能为空", groups = ValidationGroups.Login.class)
    @Size(min = 8, max = 32, message = "密码长度应为8-32位", groups = ValidationGroups.Login.class)
    private String userPassword;

    @Serial
    private static final long serialVersionUID = 3191241716373120793L;
}
