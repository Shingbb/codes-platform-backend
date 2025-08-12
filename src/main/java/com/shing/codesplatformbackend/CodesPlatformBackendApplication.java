package com.shing.codesplatformbackend;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.shing.codesplatformbackend.mapper")
public class CodesPlatformBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(CodesPlatformBackendApplication.class, args);
    }

}
