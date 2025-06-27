package com.yyby.ai.function;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Description;

import java.util.function.Function;


public class RecruitServiceFunction implements Function<RecruitServiceFunction.Request, RecruitServiceFunction.Response> {
    @Override
    public Response apply(Request request) {
        String position = "未知";
        if(request.name.contains("张三")){
            position = "算法工程师";
        }
        return new Response(position);
    }

    public record Request(String name){
    }
    public record Response(String position){

    }

    // 将本Function以Spring Bean的形式注册到容器中，支持依赖注入和调用
    @Bean
    @Description("某某是否具有资格面试")
    public Function<RecruitServiceFunction.Request,RecruitServiceFunction.Response> recruitServiceFunction(){
        return new RecruitServiceFunction();  // 返回一个新的RecruitServiceFunction实例
    }

}