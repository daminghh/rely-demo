package com.yupi.autoreply.monitor;

import com.yupi.autoreply.answerer.Answerer;
import com.yupi.autoreply.api.telegram.BotOptions;
import com.yupi.autoreply.api.telegram.TelegramApi;
import com.yupi.autoreply.model.TaskListItem;
import lombok.extern.slf4j.Slf4j;

/**
 * @author daming
 */
@Slf4j
public class TelegramMonitor extends Monitor {

//    private final TelegramApi telegramApi = new TelegramApi(new BotOptions(), openAiService);

    public TelegramMonitor(TaskListItem taskListItem) {
        super(taskListItem);
    }

    @Override
    public void onMonitor(Answerer answerer) {
        try{
            String taskName = taskListItem.getName();
            log.info("任务 {} 监控开始", taskName);
//            TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
//            botsApi.registerBot(telegramApi);
            log.info("任务 {} 监控结束", taskName);
        }catch (Exception e) {

        }

    }
}
