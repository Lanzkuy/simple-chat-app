package com.lacorp.simple_chat_app.data.entities;

public class User {

    private String user_id;
    private String username;
    private String password;
    private String fullname;

    public User() {

    }

    public User(String user_id, String username, String password, String fullname) {
        this.user_id = user_id;
        this.username = username;
        this.password = password;
        this.fullname = fullname;
    }

    public String getUser_id() {
        return user_id;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getFullname() {
        return fullname;
    }
}
