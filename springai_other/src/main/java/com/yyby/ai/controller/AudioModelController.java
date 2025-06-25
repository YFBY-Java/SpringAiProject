package com.yyby.ai.controller;

import com.alibaba.cloud.ai.dashscope.audio.DashScopeSpeechSynthesisModel;
import com.alibaba.cloud.ai.dashscope.audio.DashScopeSpeechSynthesisOptions;
import com.alibaba.cloud.ai.dashscope.audio.synthesis.SpeechSynthesisPrompt;
import com.alibaba.cloud.ai.dashscope.audio.synthesis.SpeechSynthesisResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;

@RestController
public class AudioModelController {

    private static final Logger log = LoggerFactory.getLogger(AudioModelController.class);
    @Autowired
    private DashScopeSpeechSynthesisModel dashScopeSpeechSynthesisModel;
    private static final String TEXT = "学Java，狠赚笔";
    private static final String PATH = "springai_other/src/main/resources/tts";

    @GetMapping("/tts")
    public void tts(@RequestParam(value = "msg", defaultValue = TEXT) String msg) {
        DashScopeSpeechSynthesisOptions synthesisOptions = DashScopeSpeechSynthesisOptions
                .builder()
                .withSpeed(1.0)   // 语速：1.0 为正常速度
                .withPitch(0.9)   // 音调：0.9 略低于原声
                .withVolume(60)   // 音量：0~100，60 为推荐值
                .build();

        SpeechSynthesisResponse response = dashScopeSpeechSynthesisModel.call(
                new SpeechSynthesisPrompt(msg, synthesisOptions)
        );
        File file = new File(PATH + "/output.mp3");
        try (FileOutputStream fileOutputStream = new FileOutputStream(file)) {
            ByteBuffer byteBuffer = response.getResult().getOutput().getAudio();  // 获取音频字节
            fileOutputStream.write(byteBuffer.array());
        } catch (IOException e) {
            log.error(e.toString());
        }
    }
}