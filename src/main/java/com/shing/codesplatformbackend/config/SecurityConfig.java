package com.shing.codesplatformbackend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * spring security 配置类
 *
 * @author Shing
 * date 4/8/2025
 */
@Configuration
public class SecurityConfig {

    /**
     * 注册全局密码加密器 BCryptPasswordEncoder
     * 用于用户密码加密与验证
     */
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
