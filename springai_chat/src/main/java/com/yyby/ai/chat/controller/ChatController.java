package com.yyby.ai.chat.controller;


import org.springframework.ai.chat.client.ChatClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ChatController {

    // 注入 chatclient
    private final ChatClient chatClient;
    public ChatController(ChatClient.Builder chatClientBuilder) {
        this.chatClient = chatClientBuilder.build();
    }
    // 实现简单聊天功能
    @GetMapping("/chat")
    public String chat(@RequestParam(value = "message",defaultValue = "你是一个Java开发工程师") String message){
        return chatClient.prompt()   // 提示词
                .user(message)    // 用户输入信息
                .call()  // 请求大模型
                .content();  //返回文本
    }
}