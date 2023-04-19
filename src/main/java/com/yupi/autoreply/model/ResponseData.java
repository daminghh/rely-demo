package com.yupi.autoreply.model;

import lombok.Data;

@Data
public class ResponseData {
    String id;
    String object;
    long created;
    String model;
    Choice[] choices;

    @Data
    public static class Choice {
        Delta delta;
        int index;
        String finish_reason;

        static class Delta {
            String role;
            String content;
        }
    }




}
