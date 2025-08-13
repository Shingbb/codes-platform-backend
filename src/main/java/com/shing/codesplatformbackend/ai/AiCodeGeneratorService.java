package com.shing.codesplatformbackend.ai;

import com.shing.codesplatformbackend.ai.model.HtmlCodeResult;
import com.shing.codesplatformbackend.ai.model.MultiFileCodeResult;
import dev.langchain4j.service.SystemMessage;

/**
 * @author Shing
 * date 7/8/2025
 */
public interface AiCodeGeneratorService {

    /**
     * 生成 HTML 代码
     *
     * @param userMessage 用户消息
     * @return 生成的代码结果
     */
    @SystemMessage(fromResource = "prompt/codegen-html-system-prompt.txt")
    HtmlCodeResult generateHtmlCode(String userMessage);

    /**
     * 生成多文件代码
     *
     * @param userMessage 用户消息
     * @return 生成的代码结果
     */
    @SystemMessage(fromResource = "prompt/codegen-multi-file-system-prompt.txt")
    MultiFileCodeResult generateMultiFileCode(String userMessage);

}
