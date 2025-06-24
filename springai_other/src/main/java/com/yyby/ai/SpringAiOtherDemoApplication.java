package com.yyby.ai;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(
        exclude = {
                org.springframework.ai.autoconfigure.openai.OpenAiAutoConfiguration.class
        }
)
public class SpringAiOtherDemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringAiOtherDemoApplication.class, args);
    }

}