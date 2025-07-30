package com.shing.codesplatformbackend.controller;

import com.shing.codesplatformbackend.common.BaseResponse;
import com.shing.codesplatformbackend.common.ResultUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
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
}
