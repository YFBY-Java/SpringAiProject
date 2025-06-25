package com.yyby.ai.controller;

import com.alibaba.cloud.ai.dashscope.api.DashScopeImageApi;
import com.alibaba.cloud.ai.dashscope.image.DashScopeImageModel;
import com.alibaba.cloud.ai.dashscope.image.DashScopeImageOptions;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.ai.image.ImagePrompt;
import org.springframework.ai.image.ImageResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.InputStream;
import java.net.URI;
import java.net.URL;

@RestController
public class ImageModelController {

    @Autowired
    private DashScopeImageModel imageModel;
    @GetMapping("/imageModel")
    public void getImage(
            @RequestParam(value = "msg", defaultValue = "生成一个Java的图标") String msg,
            HttpServletResponse response) {
        // 调用图像生成模型，构建 ImagePrompt 对象
        ImageResponse imageResponse = imageModel.call(
                new ImagePrompt(
                        msg,
                        DashScopeImageOptions.builder()
                                .withModel(DashScopeImageApi.DEFAULT_IMAGE_MODEL) // 使用默认图像模型
                                .withN(1)     // 生成一张图像
                                .withHeight(1024) // 图像高度
                                .withWidth(1024)  // 图像宽度
                                .build()
                )
        );
        // 从响应中获取生成的图像 URL
        String imageUrl = imageResponse.getResult().getOutput().getUrl();
        try {
            URL url = URI.create(imageUrl).toURL();   // 将图像 URL 转为 URL 对象
            // 打开输入流读取图像二进制数据
            InputStream inputStream = url.openStream();
            response.setHeader("Content-Type", MediaType.IMAGE_PNG_VALUE);  // 设置响应头，告知客户端接收的是 PNG 图像
            // 将图像数据写入响应输出流
            response.getOutputStream().write(inputStream.readAllBytes());
            // 刷新输出流，确保数据发送到客户端
            response.getOutputStream().flush();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /*
     流程总结：
     1. 接收 HTTP GET 请求，获取 msg 参数
     2. 构建 ImagePrompt 并调用 imageModel 生成图像
     3. 从 ImageResponse 获取图像 URL
     4. 打开 URL 输入流，读取图像数据
     5. 设置响应头，写入图像数据并 flush
     6. 异常捕获并包装为 RuntimeException
     */

}