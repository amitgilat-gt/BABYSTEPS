package com.amit_g.dto;

import java.util.List;

// DTO representing the response from a language model API
public class R1Response {
    private List<Choice> choices; // List of possible response choices returned by the model

    // Getter for the list of choices
    public List<Choice> getChoices() {
        return choices;
    }

    // Inner class representing one possible response choice
    public static class Choice {
        private Message message; // The message returned in this choice

        // Getter for the message
        public Message getMessage() {
            return message;
        }
    }

    // Inner class representing the content of a response message
    public static class Message {
        private String content; // The actual text content generated by the model

        // Getter for the content
        public String getContent() {
            return content;
        }
    }
}
