package com.shing.codesplatformbackend.ai;

import com.shing.codesplatformbackend.ai.model.HtmlCodeResult;
import com.shing.codesplatformbackend.ai.model.MultiFileCodeResult;
import com.shing.codesplatformbackend.common.TestBase;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * @author Shing
 * date 7/8/2025
 */
class AiCodeGeneratorServiceTest extends TestBase {

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