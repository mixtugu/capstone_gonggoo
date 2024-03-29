package com.example.dto;

import lombok.Getter;

@Getter
public class ChatMessageDTO {
    // Getters and setters
    private long from;
    private String text;
    private Integer room;

    // Constructors, Getters, and Setters
    public ChatMessageDTO() {}

    public ChatMessageDTO(long from, String text, Integer room) {
        this.from = from;
        this.text = text;
        this.room = room;
    }

}
