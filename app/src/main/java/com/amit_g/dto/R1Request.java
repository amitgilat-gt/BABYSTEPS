package com.amit_g.dto;

import java.util.List;

public class R1Request {
    public String model;
    public List<Message> messages;

    public R1Request(String model, List<Message> messages) {
        this.model = model;
        this.messages = messages;
    }

    public static class Message {
        public String role;
        public String content;

        public Message(String role, String content) {
            this.role = role;
            this.content = content;
        }
    }
}
