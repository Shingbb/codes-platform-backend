package com.shing.codesplatformbackend.controller;

import com.shing.codesplatformbackend.common.BaseResponse;
import com.shing.codesplatformbackend.common.ResultUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 接口健康测试
 *
 * @author Shing
 * date 30/7/2025
 */
@RestController
@RequestMapping("/health")
public class HealthController {

    @GetMapping("/")
    public BaseResponse<String> healthCheck() {
        return ResultUtils.success("Health Check OK");
    }

    @GetMapping("/i18n")
    public BaseResponse<String> testI18n() {
        // 返回一个带国际化消息的响应，使用成功的code和message
        return ResultUtils.success("This is the payload data");
    }

}
