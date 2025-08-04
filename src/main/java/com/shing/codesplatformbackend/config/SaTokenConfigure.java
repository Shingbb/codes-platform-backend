package com.shing.codesplatformbackend.config;

import cn.dev33.satoken.interceptor.SaInterceptor;
import cn.dev33.satoken.stp.StpUtil;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Sa-Token 拦截器配置类
 *
 * @author Shing
 * date 4/8/2025
 */
@Configuration
public class SaTokenConfigure implements WebMvcConfigurer {

    // 注册拦截器
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 注册 Sa-Token 登录拦截器，默认拦截所有接口
        registry.addInterceptor(new SaInterceptor(handler -> StpUtil.checkLogin()))
                .addPathPatterns("/**") // 注意：会自动加上 context-path，即 /api/**
                .excludePathPatterns(
                        "/api/user/login",      // 登录接口
                        "/api/user/register",   // 注册接口
                        "/api/user/code",       // 获取验证码（如图形验证码、邮箱验证码）
                        "/api/common/**",       // 公共接口（如AI模型列表、公告等）
                        "/favicon.ico",         // 避免浏览器请求干扰
                        "/error"                // Spring Boot 默认错误处理
                );
    }

}
