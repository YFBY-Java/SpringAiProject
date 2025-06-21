package com.yyby.ai.function.config;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AiConfig {
    @Bean
    public ChatClient chatClient(ChatClient.Builder builder){
        return builder.defaultSystem("你是一个Java开发工程师，精通Java后端开发相关的技术")  // 默认角色
                .build();
    }
}