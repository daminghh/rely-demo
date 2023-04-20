package com.yupi.autoreply.api.telegram;

import com.yupi.autoreply.config.OpenAiConfig;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.bots.DefaultBotOptions;

@Data
@Component
public class BotOptions extends DefaultBotOptions {



    public BotOptions(){

    }

    public BotOptions(Boolean isProxy){
        if (isProxy) {
            this.setProxyHost("127.0.0.1");
            this.setProxyPort(7890);
            this.setProxyType(DefaultBotOptions.ProxyType.HTTP);
        }
    }
}
