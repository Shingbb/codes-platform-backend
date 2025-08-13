package com.shing.codesplatformbackend.ai;

import com.shing.codesplatformbackend.ai.model.HtmlCodeResult;
import com.shing.codesplatformbackend.ai.model.MultiFileCodeResult;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Profile;

/**
 * @author Shing
 * date 7/8/2025
 */
@SpringBootTest
@Profile("local")
class AiCodeGeneratorServiceTest {

    @Resource
    private AiCodeGeneratorService aiCodeGeneratorService;

    @Test
    void generateHtmlCode() {
        HtmlCodeResult result = aiCodeGeneratorService.generateHtmlCode("做个后端程序员的工作记录小工具");
        System.out.println(result.getHtmlCode());
        System.out.println(result.getDescription());
        System.out.println( result);
        Assertions.assertNotNull(result);
    }

    @Test
    void generateMultiFileCode() {
        MultiFileCodeResult multiFileCode = aiCodeGeneratorService.generateMultiFileCode("做个代码程序员alex的留言板");
        Assertions.assertNotNull(multiFileCode);
    }
}