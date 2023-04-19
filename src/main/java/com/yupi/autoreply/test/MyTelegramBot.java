package com.yupi.autoreply.test;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.yupi.autoreply.model.ChatCompletion;
import com.yupi.autoreply.model.ChatMessageRole;
import com.yupi.autoreply.model.Message;
import okhttp3.*;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.AnswerCallbackQuery;
import org.telegram.telegrambots.meta.api.methods.ParseMode;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.File;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import java.util.concurrent.TimeUnit;

public class MyTelegramBot extends TelegramLongPollingBot {

    // 在 BotFather 中获得的 bot token
    private static final String BOT_TOKEN = "6269751823:AAEFgFXylTMaxMSGm_y__P1vNwzk0SxiATk";

    private static final String API_KEY = "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";
    private static final String  URL = "https://api.openai.com/v1/chat/completions";


    private static final OkHttpClient client = new OkHttpClient.Builder()
            .connectTimeout(300, TimeUnit.SECONDS)
            .writeTimeout(300, TimeUnit.SECONDS)
            .readTimeout(300, TimeUnit.SECONDS)
            .proxy(new Proxy(Proxy.Type.HTTP, new InetSocketAddress("127.0.0.1", 7890)))
            .build();

    private static final Map<String, Object> payload = new HashMap<String, Object>() {{
        put("model", "gpt-3.5-turbo");
        put("temperature", 0.5);
        put("top_p", 1);
        put("n", 1);
        put("stream", false);
        put("presence_penalty", 0);
        put("frequency_penalty", 0);
    }};

    // 这里填写你的 Bot 的名称，如 MyCoolBot
    private static final String BOT_NAME = "daminghahaBot";

    private static MyTelegramBot instance;


    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static synchronized MyTelegramBot getInstance() {
        if (instance == null) {
            DefaultBotOptions botOptions = new DefaultBotOptions();
            botOptions.setProxyHost("127.0.0.1");
            botOptions.setProxyPort(7890);
            botOptions.setProxyType(DefaultBotOptions.ProxyType.HTTP);
            instance = new MyTelegramBot(botOptions);
        }
        return instance;
    }

    public MyTelegramBot(DefaultBotOptions botOptions) {
        super(botOptions);
    }
    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            String messageText = update.getMessage().getText();
            Long chatId = update.getMessage().getChatId();

            // 判断是否为命令
            if (messageText.startsWith("/")) {
                String[] command = messageText.split(" ");

                // 处理不同的命令
                if ("/help".equals(command[0])) {
                    // 处理 /help 命令
                    String response = "你好，我是一名大懿的男朋友，请输入你的问题。";
                    SendMessage message = new SendMessage(chatId.toString(), response);
                    try {
                        execute(message);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else if ("/reset".equals(command[0])) {
                    // 处理 /help 命令
                    String user = update.getMessage().getFrom().getUserName();
                    String response = "好的 "+user+"，历史缓存数据现已释放，您可以请再次尝试.";
                    SendMessage message = new SendMessage(chatId.toString(), response);
                    try {
                        History.historys.remove(chatId.toString());
                        execute(message);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }else {
                    // 其他命令
                    String user = update.getMessage().getFrom().getUserName();
                    String response = "对不起 "+user+"，我不知道你在说什么，输入 /help 获取帮助。";
//                    SendMessage message = new SendMessage(chatId.toString(), response);
                    SendPhoto message = new SendPhoto(chatId.toString(),new InputFile(new File("image/dabao.jpg")));
                    try {
                        execute(message);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            } else {
                // 不是命令，回复消息
                String user = update.getMessage().getFrom().getUserName();
                String reMessage = doAnswer(messageText,chatId.toString());
                if (reMessage == null || reMessage.length() == 0) {
                    reMessage = "非常抱歉 "+user+"，我不知道您在说什么，输入 /help 获取帮助。";
                }
                SendMessage message = new SendMessage(chatId.toString(), reMessage);
                try {
                    message.setParseMode(ParseMode.MARKDOWNV2);
                    execute(message);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } else if (update.hasCallbackQuery()) {
            CallbackQuery callbackQuery = update.getCallbackQuery();
            // Handle the callback query, e.g. update message
            try {
                execute(new AnswerCallbackQuery(callbackQuery.getId()));
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        }
    }

    private String doAnswer(String prompt, String chainId) {
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
            String jsonRequestData = objectMapper.writeValueAsString(payload);
            RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), jsonRequestData);
            Request request = new Request.Builder()
                    .url(URL)
                    .addHeader("Authorization", "Bearer " + API_KEY)
                    .addHeader("Content-Type", "application/json")
                    .post(requestBody)
                    .build();
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

    @Override
    public String getBotUsername() {
        return BOT_NAME;
    }

    @Override
    public String getBotToken() {
        return BOT_TOKEN;
    }

}