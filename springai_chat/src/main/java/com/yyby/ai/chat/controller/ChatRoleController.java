package com.yyby.ai.chat.controller;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
public class ChatRoleController {

    @Autowired
    private ChatClient chatClient;

    // 非流式输出
    @GetMapping("/chatAiByRole")
    public String chatAiByRole(@RequestParam(value = "msg") String msg){
        return chatClient.prompt().user(msg).call().content();
    }

    //流式输出
    //produces 指定当请求该方法时返回的内容类型。
    @GetMapping(value = "/chatAiByRole/stream",produces = "text/html;charset=UTF-8")
    public Flux<String> chatStream(@RequestParam(value = "msg") String message){
        return chatClient.prompt().user(message).stream().content();
    }

}