package com.yupi.autoreply.api.telegram;

import com.yupi.autoreply.api.telegram.service.OpenAiService;
import com.yupi.autoreply.config.OpenAiConfig;
import com.yupi.autoreply.test.History;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
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

@Service
public class TelegramApi extends TelegramLongPollingBot {

    @Autowired
    OpenAiService openAiService;
    @Autowired
    OpenAiConfig openAiConfig;

    public  TelegramApi(BotOptions botOptions, OpenAiService openAiService,OpenAiConfig openAiConfig) {
        super(new BotOptions(openAiConfig.getIsProxy()));
        this.openAiService = openAiService;
        this.openAiConfig = openAiConfig;
    }
    private static final String BOT_NAME = "daminghahaBot";

    private static final String BOT_TOKEN = "6269751823:AAEFgFXylTMaxMSGm_y__P1vNwzk0SxiATk";


    @Override
    public String getBotUsername() {
        return BOT_NAME;
    }

    @Override
    public String getBotToken() {
        return BOT_TOKEN;
    }

    @Override
    public void onUpdateReceived(Update update) {
        System.out.println(123);
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
                String reMessage = openAiService.doAnswer(messageText,chatId.toString());
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
}
