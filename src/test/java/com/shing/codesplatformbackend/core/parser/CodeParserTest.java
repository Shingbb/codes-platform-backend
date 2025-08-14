package com.shing.codesplatformbackend.core.parser;

import com.shing.codesplatformbackend.ai.model.HtmlCodeResult;
import com.shing.codesplatformbackend.ai.model.MultiFileCodeResult;
import com.shing.codesplatformbackend.common.TestBase;
import com.shing.codesplatformbackend.core.CodeParser;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * CodeParser 测试
 *
 * @author Shing
 * date 13/8/2025
 */
class CodeParserTest extends TestBase {

    @Test
    void parseHtmlCode() {
        // 场景 1：HTML 代码块
        String codeContent = """
                ```html
                <!DOCTYPE html> <!-- 文档类型声明 -->
                <html> <!-- HTML 根标签 -->
                <head> <!-- 头部标签 -->
                    <title>测试页面标题</title> <!-- 页面标题 -->
                </head>
                <body> <!-- 页面主体 -->
                    <h1>这是一级标题</h1> <!-- 大标题 -->
                    <p>这是一个段落，用于测试解析功能。</p> <!-- 段落 -->
                </body>
                </html>
                ```
                """;
        HtmlCodeResult result1 = CodeParser.parseHtmlCode(codeContent);
        assertNotNull(result1);
        assertNotNull(result1.getHtmlCode());

        // 场景 2：无 HTML 代码块，直接原文
        String rawHtml = "<p>Direct HTML</p>";
        HtmlCodeResult result2 = CodeParser.parseHtmlCode(rawHtml);
        assertNotNull(result2);
        assertNotNull(result2.getHtmlCode());
    }

    @Test
    void parseMultiFileCode() {
        // 场景 1：同时包含 HTML + CSS + JS
        String codeContent = """
                创建一个完整的网页（包含 HTML、CSS、JS 三个文件）：
                ```html
                <!DOCTYPE html> <!-- 文档类型声明 -->
                <html> <!-- HTML 根标签 -->
                <head> <!-- 头部标签 -->
                    <title>多文件示例页面</title> <!-- 页面标题 -->
                    <link rel="stylesheet" href="style.css"> <!-- 引入外部 CSS 文件 -->
                </head>
                <body> <!-- 页面主体 -->
                    <h1>这是来自 HTML 文件的一级标题</h1> <!-- 大标题 -->
                    <p>这是来自 HTML 文件的段落，用于测试多文件解析。</p> <!-- 段落 -->
                    <script src="script.js"></script> <!-- 引入外部 JS 文件 -->
                </body>
                </html>
                ```
                ```css
                /* style.css - 定义标题样式 */
                h1 {
                    color: blue; /* 设置标题为蓝色 */
                    text-align: center; /* 标题居中 */
                }
                
                /* 段落样式 */
                p {
                    color: gray; /* 段落字体颜色 */
                }
                ```
                ```js
                // script.js - 页面加载提示
                console.log('页面加载完成'); // 控制台输出提示
                alert('欢迎访问多文件示例页面！'); // 页面弹窗提示
                ```
                文件创建完成！
                """;
        MultiFileCodeResult result1 = CodeParser.parseMultiFileCode(codeContent);
        assertNotNull(result1);
        assertNotNull(result1.getHtmlCode());
        assertNotNull(result1.getCssCode());
        assertNotNull(result1.getJsCode());
        assertTrue(result1.getHtmlCode().contains("<!DOCTYPE html>"));


        // 场景 2：只包含 HTML
        String onlyHtml = """
                ```html
                <div>Only HTML</div>
                ```
                """;
        MultiFileCodeResult result2 = CodeParser.parseMultiFileCode(onlyHtml);
        assertEquals("<div>Only HTML</div>", result2.getHtmlCode());
        assertNull(result2.getCssCode());
        assertNull(result2.getJsCode());
    }
}