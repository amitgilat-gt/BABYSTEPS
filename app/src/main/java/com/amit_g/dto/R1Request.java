package com.amit_g.dto;

import java.util.List;

// DTO representing a request to a language model API
public class R1Request {
    public String model; // Name of the model to use (e.g., "gpt-4")
    public List<Message> messages; // List of messages forming the conversation context

    // Constructor for the request
    public R1Request(String model, List<Message> messages) {
        this.model = model;
        this.messages = messages;
    }

    // Inner class representing a single message in the conversation
    public static class Message {
        public String role;    // Role of the message sender (e.g., "user", "assistant")
        public String content; // Content of the message

        // Constructor for a message
        public Message(String role, String content) {
            this.role = role;
            this.content = content;
        }
    }
}
