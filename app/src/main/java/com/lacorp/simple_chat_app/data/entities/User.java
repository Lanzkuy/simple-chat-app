package com.lacorp.simple_chat_app.data.entities;

public class User {

    private String user_id;
    private String username;
    private String password;
    private String fullname;

    public User() {

    }

    public User(String user_id, String username, String password, String fullname) {
        this.setUser_id(user_id);
        this.setUsername(username);
        this.setPassword(password);
        this.setFullname(fullname);
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

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }
}
