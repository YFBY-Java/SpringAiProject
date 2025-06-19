package com.yyby.ai.chat.controller;

import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ChatModelController {

    @Autowired
    private ChatModel chatModel;

    @GetMapping
    public String chat(@RequestParam("msg")String msg) {
        return chatModel.call(msg);
    }

    @GetMapping("/openai")
    public String openai(@RequestParam("msg")String msg) {
        ChatResponse call = chatModel.call(
                new Prompt(
                        msg,
                        OpenAiChatOptions.builder()
                                .model("deepseek-chat")  // 指定模型
                                .temperature(0.8)
                                .build()
                )
        );
        return call.getResult().getOutput().getContent();
    }
}