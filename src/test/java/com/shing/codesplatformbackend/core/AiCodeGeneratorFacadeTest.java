package com.shing.codesplatformbackend.core;

import com.shing.codesplatformbackend.common.TestBase;
import com.shing.codesplatformbackend.model.enums.CodeGenTypeEnum;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;

import java.io.File;

/**
 * @author Shing
 * date 13/8/2025
 */
class AiCodeGeneratorFacadeTest extends TestBase {

    @Resource
    private AiCodeGeneratorFacade aiCodeGeneratorFacade;

    @Test
    void generateAndSaveCode() {
        File file = aiCodeGeneratorFacade.generateAndSaveCode("任务记录网站", CodeGenTypeEnum.MULTI_FILE);
        Assertions.assertNotNull(file);
    }

    @Test
    void  generateAndSaveCodeStream() {
        Flux<String> stream = aiCodeGeneratorFacade.generateAndSaveCodeStream("任务记录网站", CodeGenTypeEnum.MULTI_FILE);
        Assertions.assertNotNull(stream);
    }
}