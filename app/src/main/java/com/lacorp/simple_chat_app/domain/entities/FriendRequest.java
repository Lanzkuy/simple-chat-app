package com.lacorp.simple_chat_app.domain.entities;

public class FriendRequest {

    private String friend_request_id;
    private String user_id;
    private String username;

    public FriendRequest() {

    }

    public FriendRequest(String friend_request_id, String user_id, String username) {
        this.setFriend_request_id(friend_request_id);
        this.setUser_id(user_id);
        this.setUsername(username);
    }

    public String getFriend_request_id() {
        return friend_request_id;
    }

    public String getUser_id() {
        return user_id;
    }

    public String getUsername() {
        return username;
    }

    public void setFriend_request_id(String friend_request_id) {
        this.friend_request_id = friend_request_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
