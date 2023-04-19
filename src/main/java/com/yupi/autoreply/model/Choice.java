package com.yupi.autoreply.model;

import lombok.Data;

import java.util.List;

@Data
public class Choice {
    private String id;
    private String object;
    private long created;
    private String model;
    private List<Option> choices;

    @Data
    public static class Option {
        Delta delta;
        int index;
        String finish_reason;

        @Data
        public static class Delta {
            String role;
            String content;
        }
    }


}