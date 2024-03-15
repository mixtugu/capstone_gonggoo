package com.example.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Message {
    private String from;
    private String text;
    private LocalDateTime timestamp;
    private String formattedTimestamp;


    public Message() {
        this.formattedTimestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm"));
    }

    public Message(String from, String text) {
        this.from = from;
        this.text = text;
        this.timestamp = LocalDateTime.now();
        this.formattedTimestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm"));
    }


    // Getters and Setters
    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public LocalDateTime getTimestamp() { return timestamp; }

    public String getFormattedTimestamp() {return formattedTimestamp;}


}
