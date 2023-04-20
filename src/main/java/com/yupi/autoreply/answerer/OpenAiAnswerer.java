package com.yupi.autoreply.answerer;


import com.yupi.autoreply.api.openai.OpenAiApi;
import com.yupi.autoreply.api.openai.model.CreateCompletionRequest;

import com.yupi.autoreply.config.OpenAiConfig;

import com.yupi.autoreply.model.ChatCompletion;
import com.yupi.autoreply.model.ChatMessageRole;
import com.yupi.autoreply.test.History;
import com.yupi.autoreply.utils.SpringContextUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * OpenAi 回答者
 *
 * @author <a href="https://github.com/liyupi">程序员鱼皮</a>
 * @from <a href="https://yupi.icu">编程导航知识星球</a>
 */
@Slf4j
@Service
public class OpenAiAnswerer implements Answerer {

    @Autowired
    private OpenAiConfig openAiConfig;

    @Override
    public String doAnswer(String prompt) {
        CreateCompletionRequest request = new CreateCompletionRequest();
        request.setPrompt(prompt);
        request.setModel(openAiConfig.getModel());
        request.setTemperature(0);
        request.setMax_tokens(1024);

//        ChatCompletion.Choice.Message response = openAiApi.createCompletion1(request, openAiConfig.getApiKey());
//        return response.getContent();
        return null;
    }

    @Override
    public String doAnswer(String prompt, String chainId) {
//
//        CreateCompletionRequest request = new CreateCompletionRequest();
//        request.setPrompt(prompt);
//        request.setModel(openAiConfig.getModel());
//        request.setTemperature(0);
//        request.setMax_tokens(1024);
//
//        List<ChatCompletion.Choice.Message> messages = History.historys.computeIfAbsent(chainId, k -> new ArrayList<>());
//        messages.add(new ChatCompletion.Choice.Message(ChatMessageRole.USER.value(), prompt));
//        if (messages.size() == 1) {
//            StringBuilder systemMessageBuilder = new StringBuilder("I am Dayi's boyfriend, powered by Artificial Intelligence. ");
//            systemMessageBuilder.append("I can answer your questions and provide some help and advice.");
//            ChatCompletion.Choice.Message systemMessage = new ChatCompletion.Choice.Message(ChatMessageRole.SYSTEM.value(), systemMessageBuilder.toString());
//            messages.add(0,systemMessage);
//        }
//        ChatCompletion.Choice.Message response = openAiApi.createCompletion1(request, openAiConfig.getApiKey(),History.historys.get(chainId));
//        messages.add(response);
////        History.historys.get(chainId).add(response);
//        History.historys.get(chainId);
//        return response.getContent();
        return null;
    }


}
