package com.yyby.ai.controller;

import com.alibaba.cloud.ai.dashscope.chat.DashScopeChatOptions;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.model.Generation;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
public class ChatController {

    @Autowired
    private ChatModel chatModel;

    @Autowired
    private VectorStore vectorStore;

    @GetMapping("/ai/agent")
// 定义HTTP GET接口，路径为/ai/agent，接收参数query
    public String agent(@RequestParam(value = "query") String query) {
        // 在向量数据库中检索与用户query最相似的Document列表
        List<Document> documents = vectorStore.similaritySearch(query);
        String info = "";
        if (documents.size() > 0) {
            info = documents.get(0).getContent();
        }

        // 构造系统级的prompt，定义助手角色、目标和限制
        String systemPrompt = """  
        角色与目标：你是一个招聘助手，会针对用户的问题，结合候选人经历，岗位匹配度等专业知识，给用户提供指导。
        指导原则：你需要确保给出的建议合理科学，不会对候选人的表现有言论侮辱。
        限制：在提供建议时，需要强调在个性建议方面用户仍然需要线下寻求专业咨询。
        澄清：在与用户交互过程中，你需要明确回答用户关于招聘方面的问题，对于非招聘方面的问题，你的回应是‘我只是一个招聘助手，不能回答这个问题哦’。
        个性化：在回答时，你需要以专业可靠的预期回答，偶尔可以带点幽默感。调节气氛。
        给你提供一个数据参考，并且给你调用岗位投递检索工具
        请你跟进数据参考与工具返回结果回复用户的请求。
        """;

        // 构造用户级的prompt模板，将info和query占位填入
        String userPrompt = """  
        给你提供一些数据参考：{info},请回答我的问题：{query}。
        请你跟进数据参考与工具返回结果回复用户的请求。
        """;
        // 将系统prompt封装为SystemMessage
        SystemMessage systemMessage = new SystemMessage(systemPrompt);
        // 用PromptTemplate处理用户prompt模板
        PromptTemplate promptTemplate = new PromptTemplate(userPrompt);
        // 用映射将info和query填充到模板中，生成Message
        Message userMessage = promptTemplate.createMessage(Map.of(
                "info", info,
                "query", query
        ));
        // 将用户message和系统message合并为最终的Prompt
        Prompt prompt = new Prompt(List.of(userMessage, systemMessage));
        // 配置DashScopeChatOptions以启用recruitServiceFunction函数调用能力
        DashScopeChatOptions.builder()
                .withFunctions(Set.of("recruitServiceFunction"))
                .build();
        // 调用chatModel执行对话，获得生成结果列表
        List<Generation> results = chatModel.call(prompt).getResults();
        // 将所有结果中的文本内容拼接并返回给客户端
        return results.stream()
                .map(x -> x.getOutput().getContent())
                .collect(Collectors.joining());
    }
}