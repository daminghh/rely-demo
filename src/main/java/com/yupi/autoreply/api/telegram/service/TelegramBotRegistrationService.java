package com.yupi.autoreply.api.telegram.service;

import com.yupi.autoreply.api.telegram.TelegramApi;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

import javax.annotation.PostConstruct;

@Service
public class TelegramBotRegistrationService {
    private final TelegramApi telegramApi;


    public TelegramBotRegistrationService(TelegramApi telegramApi) {
        this.telegramApi = telegramApi;
    }

    @PostConstruct
    public void registerBot() {
        try {
            TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
            botsApi.registerBot(telegramApi);
            System.out.println("启动了");
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}
