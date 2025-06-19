package com.yyby.ai.chat.controller;

import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.model.Generation;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.SystemPromptTemplate;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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

    // 提示词操作
    @GetMapping("/prompt")
    public String prompt(@RequestParam("name") String name,@RequestParam("voice") String voice){
        // 设置用户输入信息
        String userMsg = "给我推荐南京的三种甜食";
        UserMessage userMessage = new UserMessage(userMsg);
        // 设置系统提示信息
        String systemMsg = "你是一个资深的南京美食爱好者，你的名字是{name}，你熟悉南京的一切美食，" +
                "你应该用你的名字和{voice}的饮食习惯回复用户的请求";
        // 使用Prompt Template设置相关信息
        SystemPromptTemplate systemPromptTemplate = new SystemPromptTemplate(systemMsg);
        // 替换占位符
        Message message = systemPromptTemplate
                .createMessage(Map.of("name", name, "voice", voice));
        //使用prompt封装
        Prompt prompt = new Prompt(List.of(userMessage,message));
        // 调用chatModel方法
        ChatResponse chatResponse = chatModel.call(prompt);
        List<Generation> results = chatResponse.getResults();
        return results.stream() // 对结果集合results进行流处理
                .map(x -> x.getOutput().getContent()) // 提取每个元素的output中的content字段
                .collect(Collectors.joining("")); // 将所有content字符串连接为一个完整字符串，之间无分隔符

    }
}