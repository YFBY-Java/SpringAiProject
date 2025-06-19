package com.yyby.ai.controller;

import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ChatDeepSeekController {

    @Autowired
    private OpenAiChatModel chatModel;

    @GetMapping("/hello")
    public String generate(@RequestParam(value = "message" , defaultValue = "hello") String message) {
        String result = chatModel.call(message);
        System.out.println(result);
        return result;
    }

}