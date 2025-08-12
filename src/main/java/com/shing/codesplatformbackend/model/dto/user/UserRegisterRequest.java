package com.shing.codesplatformbackend.model.dto.user;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * 用户注册请求
 *
 * @author Shing
 * date 4/8/2025
 */
@Data
public class UserRegisterRequest implements Serializable {

    /**
     * 账号
     */
    @NotBlank(message = "账号不能为空", groups = ValidationGroups.Create.class)
    @Size(min = 4, max = 20, message = "账号长度应为4-20位", groups = ValidationGroups.Create.class)
    private String userAccount;

    /**
     * 密码
     */
    @NotBlank(message = "密码不能为空", groups = ValidationGroups.Create.class)
    @Size(min = 8, max = 32, message = "密码长度应为8-32位", groups = ValidationGroups.Create.class)
    @Pattern(
            regexp = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d!@#$%^&*]{8,32}$",
            message = "密码必须包含字母和数字，可包含特殊字符",
            groups = ValidationGroups.Create.class
    )
    private String userPassword;

    /**
     * 确认密码
     */
    @NotBlank(message = "确认密码不能为空", groups = ValidationGroups.Create.class)
    @NotBlank(message = "确认密码不能为空")
    private String checkPassword;

    @Serial
    private static final long serialVersionUID = 3191241716373120793L;
}
