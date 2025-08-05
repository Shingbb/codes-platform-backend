# 🧠 CodesPlatform -  AI 代码生成平台

CodesPlatform 是一款面向新时代开发者的 **AI 开发实战平台**，融合 AI 智能体、代码生成与 Prompt 工作流等前沿技术，基于企业级架构设计，致力于让开发者更高效地编写、重构、理解代码。

> 🚀 基于 Spring Boot 3 + LangChain4j + Vue 3 全栈架构，聚焦企业级应用落地。

---

## ✨ 项目亮点

- 🧱 **企业级架构设计**：前后端解耦，模块清晰，符合微服务设计规范。
- 🧠 **AI 智能体支持**：集成 [LangChain4j](https://github.com/langchain4j/langchain4j)，支持链式调用、多 Agent 调度。
- ⚡ **代码生成能力**：自然语言生成 Controller、Service、DTO、工具类等各类 Java/Vue 代码。
- 🧩 **工作流能力**：支持 Prompt 组合，构建 AI 工具链。
- 🔐 **完善的权限系统**：基于 JWT + RBAC 模型。
- 📦 **插件式模型切换**：支持 OpenAI、Ollama、ChatGLM、Qwen 等大模型接入。

---

## 🏗️ 项目结构

```
codes-platform/
├── codes-platform-backend     # Spring Boot 3 后端主工程
│   ├── ai-core                # AI 智能核心模块（封装 LangChain4j）
│   ├── code-gen               # 代码生成模块
│   ├── user-center            # 用户系统模块
│   ├── workflow-engine        # AI 工作流支持（可扩展）
│   ├── api-gateway            # 接口网关（可选微服务部署）
│   ├── infra-common           # 通用封装：统一响应、异常处理、工具类等
├── codes-platform-web         # Vue 3 前端应用
├── docs                       # 项目文档与示例
└── README.md                  # 项目说明文件（本文件）

```


## 🧠 功能概览
```
| 功能模块          | 描述                         |
| ------------- | -------------------------- |
| ✍️ 智能代码生成     | 输入自然语言，生成高质量 Java / Vue 代码 |
| 🤖 智能体开发      | 封装 AI Agent，支持链式工具调度       |
| 🔄 Prompt 工作流 | 自定义流程组合，实现自动文档生成、代码审查等     |
| 🧰 工具集成       | 包含 AI 工具、数据库工具、接口调试等       |
| 🧑‍💻 用户系统    | 注册 / 登录 / 权限校验 / 审计日志      |
| 📊 管理后台       | 查看用户调用数据、日志记录、模型管理（开发中）    |
| 🛠️ 插件系统      | 支持多种大模型接入，如 OpenAI、Ollama、ChatGLM、Qwen 等 |

```
