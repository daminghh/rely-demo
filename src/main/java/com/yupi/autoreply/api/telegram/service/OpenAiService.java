package com.yupi.autoreply.api.telegram.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yupi.autoreply.config.OpenAiConfig;
import com.yupi.autoreply.model.ChatCompletion;
import com.yupi.autoreply.model.ChatMessageRole;
import com.yupi.autoreply.model.Message;
import com.yupi.autoreply.test.History;
import okhttp3.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
public class OpenAiService {

    @Autowired
    private OpenAiConfig openAiConfig;

//    private static final OkHttpClient client = new OkHttpClient.Builder()
//            .connectTimeout(300, TimeUnit.SECONDS)
//            .writeTimeout(300, TimeUnit.SECONDS)
//            .readTimeout(300, TimeUnit.SECONDS)
//            .proxy(new Proxy(Proxy.Type.HTTP, new InetSocketAddress("127.0.0.1", 7890)))
//            .build();

    private static final Map<String, Object> payload = new HashMap<String, Object>() {{
        put("model", "gpt-3.5-turbo");
        put("temperature", 0.5);
        put("top_p", 1);
        put("n", 1);
        put("stream", false);
        put("presence_penalty", 0);
        put("frequency_penalty", 0);
    }};

    public String doAnswer(String prompt, String chainId) {
        List<Message> messages = History.historys.computeIfAbsent(chainId, k -> new ArrayList<>());
        messages.add(new Message(ChatMessageRole.USER.value(), prompt));
        if (messages.size() == 1) {
            StringBuilder systemMessageBuilder = new StringBuilder("I am Dayi's boyfriend, powered by Artificial Intelligence. ");
            systemMessageBuilder.append("I can answer your questions and provide some help and advice.");
            Message systemMessage = new Message(ChatMessageRole.SYSTEM.value(), systemMessageBuilder.toString());
            messages.add(0,systemMessage);
        }
        Message response = createCompletion(History.historys.get(chainId));
        if (response == null) {
            // 情况历史
            messages.clear();
            return "[Local Message] Reduce the length. 本次输入过长，或历史数据过长. 历史缓存数据现已释放，您可以请再次尝试.";
        }
        messages.add(response);
        History.historys.get(chainId);
        return response.getContent();
    }

    private Message createCompletion( List<Message> history) {
        payload.put("messages", history);
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            String jsonRequestData = objectMapper.writeValueAsString(payload);
            RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), jsonRequestData);
            Request request = new Request.Builder()
                    .url("https://api.openai.com/v1/chat/completions")
                    .addHeader("Authorization", "Bearer " + openAiConfig.getApiKey())
                    .addHeader("Content-Type", "application/json")
                    .post(requestBody)
                    .build();
            OkHttpClient.Builder clientBuilder = new OkHttpClient.Builder()
                    .connectTimeout(300, TimeUnit.SECONDS)
                    .writeTimeout(300, TimeUnit.SECONDS)
                    .readTimeout(300, TimeUnit.SECONDS);

            if (openAiConfig.getIsProxy()) {
                clientBuilder = clientBuilder.proxy(new Proxy(Proxy.Type.HTTP, new InetSocketAddress("127.0.0.1", 7890)));
            }

            OkHttpClient client = clientBuilder.build();
            try (Response response = client.newCall(request).execute()) {
                if (!response.isSuccessful()) {
                    throw new IOException("Unexpected code " + response);
                }
                String responseJson = response.body().string();
                ChatCompletion chatCompletion = objectMapper.readValue(responseJson, ChatCompletion.class);
                return chatCompletion.getChoices().get(0).getMessage();
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }
}
