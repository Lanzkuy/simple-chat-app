package com.lacorp.simple_chat_app.domain.entities;

public class Friend {

    private String friend_id;
    private String user_id;
    private String username;
    private String fullname;

    public Friend() {

    }

    public Friend(String friend_id, String user_id, String username, String fullname) {
        this.setFriend_id(friend_id);
        this.setUser_id(user_id);
        this.setUsername(username);
        this.setFullname(fullname);
    }

    public String getFriend_id() {
        return friend_id;
    }

    public String getUser_id() {
        return user_id;
    }

    public String getUsername() {
        return username;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFriend_id(String friend_id) {
        this.friend_id = friend_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }
}
