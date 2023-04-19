package com.yupi.autoreply.api.openai;

import cn.hutool.http.HttpRequest;
import cn.hutool.json.JSONUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;

import com.yupi.autoreply.api.openai.model.CreateCompletionRequest;
import com.yupi.autoreply.api.openai.model.CreateCompletionResponse;
import com.yupi.autoreply.common.ErrorCode;
import com.yupi.autoreply.exception.BusinessException;
import com.yupi.autoreply.model.ChatCompletion;
import com.yupi.autoreply.test.History;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import java.io.IOException;
import java.net.*;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * OpenAi 接口
 * <a href="https://platform.openai.com/docs/api-reference">参考文档</a>
 *
 * @author <a href="https://github.com/liyupi">程序员鱼皮</a>
 * @from <a href="https://yupi.icu">编程导航知识星球</a>
 **/
@Service
public class OpenAiApi {

    /**
     * 补全
     *
     * @param request
     * @param openAiApiKey
     * @return
     */
    public CreateCompletionResponse createCompletion(CreateCompletionRequest request, String openAiApiKey) {
        if (StringUtils.isBlank(openAiApiKey)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "未传 openAiApiKey");
        }
//        String url = "https://api.openai.com/v1/completions";
        String url = "https://api.openai.com/v1/chat/completions";
        String json = JSONUtil.toJsonStr(request);
        String result = HttpRequest.post(url)
                .header("Authorization", "Bearer " + openAiApiKey)
                .body(json)
                .execute()
                .body();
        return JSONUtil.toBean(result, CreateCompletionResponse.class);
    }

//    public ChatCompletion.Choice.Message createCompletion1(CreateCompletionRequest completionRequest, String apiKey, List<ChatCompletion.Choice.Message>listHistory) {
//
//        String url = "https://api.openai.com/v1/chat/completions";
//        Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress("127.0.0.1", 7890));
//        OkHttpClient client = new OkHttpClient.Builder()
//                .connectTimeout(300, TimeUnit.SECONDS)
//                .writeTimeout(300, TimeUnit.SECONDS)
//                .readTimeout(300, TimeUnit.SECONDS)
//                .proxy(proxy)
//                .build();
//
//        Gson gson = new Gson();
//
//        // 将 requestData 转换为 JSON 字符串
//        String jsonRequestData = gson.toJson(generatePayload(completionRequest.getPrompt(),
//                completionRequest.getTop_p(),
//                completionRequest.getTemperature(),
//                listHistory,
//                completionRequest.getSystemPrompt(),
//                false));
//
//
//        okhttp3.RequestBody requestBody = okhttp3.RequestBody
//                .create(MediaType.parse("application/json; charset=utf-8"),
//                        jsonRequestData);
//
//        Request request = new Request.Builder()
//                .url(url)
//                .addHeader("Authorization", "Bearer " + apiKey)
//                .addHeader("Content-Type", "application/json")
//                .post(requestBody)
//                .build();
//
//        try (Response response = client.newCall(request).execute()) {
//            if (!response.isSuccessful()) {
//                throw new IOException("Unexpected code " + response);
//            }
//            StringBuilder res = new StringBuilder();
//            String responseJson = response.body().string();
//
//            // 创建 ObjectMapper 实例
//            ObjectMapper objectMapper = new ObjectMapper();
//
//            // 将 JSON 字符串转换为 Message 对象
//            ChatCompletion chatCompletion = objectMapper.readValue(responseJson, ChatCompletion.class);
//            System.out.println(chatCompletion.getChoices().get(0).getMessage().toString());
//            return chatCompletion.getChoices().get(0).getMessage();
//        } catch (IOException e) {
//            System.out.println(e.getMessage());
//        }
//
//        return null;
//    }
//
//    public Map<String, Object> generatePayload(String inputs, Integer top_p, Integer temperature, List<ChatCompletion.Choice.Message> history, String system_prompt, boolean stream) {
//
////        String timeout_bot_msg = "[Local Message] Request timeout. Network error. Please check proxy settings in config.py." +
////                "网络错误，检查代理服务器是否可用，以及代理设置的格式是否正确，格式须是[协议]://[地址]:[端口]，缺一不可。";
////
////
////        int conversation_cnt = history.size() / 2;
////
////        List<Map<String, String>> messages = new ArrayList<>();
////        Map<String, String> system_message = new HashMap<>();
////        system_message.put("role", "system");
////        system_message.put("content", system_prompt);
////        messages.add(system_message);
////
////        if (conversation_cnt > 0) {
////            for (int index = 0; index < conversation_cnt*2; index+=2) {
////                Map<String, String> what_i_have_asked = new HashMap<>();
////                what_i_have_asked.put("role", "user");
////                what_i_have_asked.put("content", history.get(index));
////                Map<String, String> what_gpt_answer = new HashMap<>();
////                what_gpt_answer.put("role", "assistant");
////                what_gpt_answer.put("content", history.get(index+1));
////                if (!what_i_have_asked.get("content").equals("")) {
////                    if (what_gpt_answer.get("content").equals("")) {
////                        continue;
////                    }
////                    if (what_gpt_answer.get("content").equals(timeout_bot_msg)) {
////                        continue;
////                    }
////                    messages.add(what_i_have_asked);
////                    messages.add(what_gpt_answer);
////                } else {
////                    messages.get(messages.size() - 1).put("content", what_gpt_answer.get("content"));
////                }
////            }
////        }
////
////        Map<String, String> what_i_ask_now = new HashMap<>();
////        what_i_ask_now.put("role", "user");
////        what_i_ask_now.put("content", inputs);
////        messages.add(what_i_ask_now);
//
//
//        Map<String, Object> payload = new HashMap<>();
//        payload.put("model", "gpt-3.5-turbo");
//        payload.put("messages", history);
//        payload.put("temperature", temperature);
//        payload.put("top_p", top_p);
//        payload.put("n", 1);
//        payload.put("stream", stream);
//        payload.put("presence_penalty", 0);
//        payload.put("frequency_penalty", 0);
//
//        return payload;
//    }
//
////    public ChatMessage createCompletion2(String input) {
////        final List<ChatMessage> messages = new ArrayList<>();
////        final ChatMessage systemMessage = new ChatMessage(ChatMessageRole.SYSTEM.value(), "I am Dayi's boyfriend, powered by Artificial Intelligence. I can answer your questions and provide some help and advice.");
////        final ChatMessage systemMessage1 = new ChatMessage(ChatMessageRole.USER.value(), input);
////        messages.add(systemMessage);
////        messages.add(systemMessage1);
////
////        ChatCompletionRequest chatCompletionRequest = ChatCompletionRequest
////                .builder()
////                .model("gpt-3.5-turbo")
////                .messages(messages)
////                .n(1)
////                .maxTokens(50)
////                .logitBias(new HashMap<>())
////                .build();
////
////        List<ChatCompletionChoice> choices = service.createChatCompletion(chatCompletionRequest).getChoices();
////        choices.forEach(System.out::println);
////        return choices.get(0).getMessage();
////    }
}
