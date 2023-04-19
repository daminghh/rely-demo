package com.yupi.autoreply.test;

import cn.hutool.json.JSONUtil;
import com.google.gson.Gson;
import com.yupi.autoreply.api.openai.model.CreateCompletionResponse;
import com.yupi.autoreply.model.Choice;
import com.yupi.autoreply.model.ResponseData;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

public class Main {
    public static void main(String[] args)  {
        Gson gson = new Gson();

        String str01= "data: {\"id\":\"chatcmpl-75W92EbM66rmCmCHq6Q6L0BWIxVxH\",\"object\":\"chat.completion.chunk\",\"created\":1681549560,\"model\":\"gpt-3.5-turbo-0301\",\"choices\":[{\"delta\":{\"role\":\"assistant\"},\"index\":0,\"finish_reason\":null}]}";
// 截取 JSON 字符串
        String jsonStr = str01.substring(str01.indexOf("{"), str01.lastIndexOf("}") + 1);
        // 解析 JSON 字符串
        Choice choice = gson.fromJson(jsonStr,Choice.class);
        System.out.println(choice.getChoices().get(0).getDelta().getContent());

    }
}
