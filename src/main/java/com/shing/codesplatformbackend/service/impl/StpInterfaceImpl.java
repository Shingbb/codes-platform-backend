package com.shing.codesplatformbackend.service.impl;

import cn.dev33.satoken.stp.StpInterface;
import com.shing.codesplatformbackend.model.entity.User;
import com.shing.codesplatformbackend.service.UserService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * sa-token自定义权限加载接口实现类，实现鉴权接口
 *
 * @author Shing
 * date 20/8/2025
 */
@Component
// 保证此类被 SpringBoot 扫描，完成 Sa-Token 的自定义权限验证扩展
public class StpInterfaceImpl implements StpInterface {

    @Resource
    private UserService userService;

    /**
     * 返回一个账号所拥有的权限码集合
     */
    @Override
    public List<String> getPermissionList(Object loginId, String loginType) {
        // 从数据库或缓存中查询用户权限
        List<String> permissionList = new ArrayList<>();
        // 这里可以根据实际业务需求添加权限
        permissionList.add("user.get");
        return permissionList;
    }

    /**
     * 返回一个账号所拥有的角色标识集合 (权限与角色可分开校验)
     */
    @Override
    public List<String> getRoleList(Object loginId, String loginType) {
        // 根据loginId查询用户信息
        Long userId = Long.valueOf(String.valueOf(loginId));
        User user = userService.getById(userId);

        List<String> roleList = new ArrayList<>();
        if (user != null && user.getUserRole() != null) {
            roleList.add(user.getUserRole());
        }
        return roleList;
    }

}