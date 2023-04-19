//package com.yupi.autoreply.answerer;
//
//
//import com.yupi.autoreply.api.openai.OpenAiApi;
//
//import com.yupi.autoreply.test.History;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.stereotype.Component;
//
//import java.util.ArrayList;
//
///**
// * OpenAi 回答者
// *
// * @author <a href="https://github.com/liyupi">程序员鱼皮</a>
// * @from <a href="https://yupi.icu">编程导航知识星球</a>
// */
//@Slf4j
//@Component
//public class OpenAiAnswerer1 {
//
//    OpenAiApi openAiApi = new OpenAiApi();
//
//    public String doAnswer(String prompt, String chainId) {
//        List<ChatMessage> messages;
//        if (History.historys.containsKey(chainId)) {
//                messages = History.historys.get(chainId);
//        } else {
//            messages = new ArrayList<>();
//            ChatMessage systemMessage = new ChatMessage(ChatMessageRole.SYSTEM.value(), "I am Dayi's boyfriend, powered by Artificial Intelligence. I can answer your questions and provide some help and advice.");
//            messages.add(systemMessage);
//            History.historys.put(chainId, messages);
//        }
//        ChatMessage answer = openAiApi.createCompletion2(prompt);
//        messages.add(answer);
//        History.historys.get(chainId).add(answer);
//        return answer.getContent();
//    }
//
//
//}
