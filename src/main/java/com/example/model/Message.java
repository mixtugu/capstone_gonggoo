package com.example.model;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Getter@Setter
public class Message {
    private String from;
    private String text;
    private long from_id;
    private Integer room_id;
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

    public Message(String from, String text, long from_id, Integer room_id) {
        this.from = from;
        this.text = text;
        this.from_id = from_id;
        this.room_id = room_id;
        this.timestamp = LocalDateTime.now();
        this.formattedTimestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm"));
    }


    // Getters and Setters
    public String getFrom() {
        return from;
    }

    public long getFromId() { return from_id; }

    public Integer getRoomId() { return room_id; }

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
