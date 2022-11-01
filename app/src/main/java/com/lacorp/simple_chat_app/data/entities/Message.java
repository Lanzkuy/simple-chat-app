package com.lacorp.simple_chat_app.data.entities;

public class Message {

    private String sender_id;
    private String message;

    public Message() {

    }

    public Message(String sender_id, String message) {
        this.setSender_id(sender_id);
        this.setMessage(message);
    }

    public String getSender_id() {
        return sender_id;
    }

    public String getMessage() {
        return message;
    }

    public void setSender_id(String sender_id) {
        this.sender_id = sender_id;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
