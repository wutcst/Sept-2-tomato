package com.example.back.sys.entity;

import lombok.Data;

@Data
public class Message {
    private String type;
    private String text;
    private String user;

    public Message(String type, String text) {
        this.type = type;
        this.text = text;
    }
}
