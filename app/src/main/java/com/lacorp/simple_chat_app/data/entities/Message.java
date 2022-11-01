package com.lacorp.simple_chat_app.data.entities;

import java.util.Date;

public class Message {

    private String sender_id;
    private String message;
    private Date last_message_time;

    public Message() {

    }

    public Message(String sender_id, String message, Date last_message_time) {
        this.setSender_id(sender_id);
        this.setMessage(message);
        this.setLast_message_time(last_message_time);
    }

    public String getSender_id() {
        return sender_id;
    }

    public String getMessage() {
        return message;
    }

    public Date getLast_message_time() {
        return last_message_time;
    }

    public void setSender_id(String sender_id) {
        this.sender_id = sender_id;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setLast_message_time(Date last_message_time) {
        this.last_message_time = last_message_time;
    }
}
