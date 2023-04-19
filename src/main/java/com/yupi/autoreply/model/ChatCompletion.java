package com.yupi.autoreply.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChatCompletion {
    private String id;
    private String object;
    private long created;
    private String model;
    private Usage usage;
    private List<Choice> choices;

    // Constructor, getters, and setters
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static  class Usage {
        private int prompt_tokens;
        private int completion_tokens;
        private int total_tokens;

        // Constructor, getters, and setters
    }
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static  class Choice {
        private Message message;
        private String finish_reason;
        private int index;

        // Constructor, getters, and setters

    }
}







