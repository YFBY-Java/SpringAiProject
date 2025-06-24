package com.yyby.ai.controller;

import com.alibaba.cloud.ai.dashscope.chat.DashScopeChatOptions;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.memory.InMemoryChatMemory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AlibabaController {
    private static final String DEFAULT_PROMPT = "你是一个精通Java的后端开发专家，请根据用户提问回答！";
    private final ChatClient dashScopeChatClient;

    /**
     * Advisor是在Spring AI中用于拦截ChatClient调用链、插入自定义逻辑的拦截器，
     * 通过实现Advisor接口可以在发送消息前后执行附加操作，比如存储对话上下文、记录日志或接入埋点
     * 在代码中通过chatClientBuilder.defaultAdvisors(你的Advisor实现)将一个或多个Advisor注册到ChatClient实例中
     */
    // 构造方法注入 ChatClient.Builder 并完成自定义配置
    public AlibabaController(ChatClient.Builder chatClientBuilder) {
        this.dashScopeChatClient = chatClientBuilder
                .defaultSystem(DEFAULT_PROMPT)   // 设置全局系统提示词
                // 添加聊天记忆 Advisor，使用内存存储对话上下文
                .defaultAdvisors(
                        new MessageChatMemoryAdvisor(new InMemoryChatMemory())
                )
                // 添加日志记录 Advisor，输出调用日志
                .defaultAdvisors(
                        new SimpleLoggerAdvisor()
                )
                // 配置 DashScope 特定选项，例如 topP 截断概率
                .defaultOptions(
                        DashScopeChatOptions.builder()
                                .withTopP(0.7)
                                .build()
                )
                // 构建最终可用的 ChatClient
                .build();
    }

    @GetMapping("/alibaba/chat")
    public String simpleChat(String query) {
        // 将用户输入发送给 dashScopeChatClient，执行同步调用并返回内容
        return dashScopeChatClient.prompt(query).call().content();
    }
}