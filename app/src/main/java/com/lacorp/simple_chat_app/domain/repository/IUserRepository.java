package com.lacorp.simple_chat_app.domain.repository;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.Query;
import com.lacorp.simple_chat_app.domain.entities.Friend;
import com.lacorp.simple_chat_app.domain.entities.FriendRequest;
import com.lacorp.simple_chat_app.domain.entities.User;

public interface IUserRepository {

    DocumentReference getUserByID(String user_id);

    Query getUserByUsername(String username);

    Query getFriends(String user_id);

    Query getFriendRequests(String user_id);

    Task<Void> addFriend(String user_id, FriendRequest friendRequest);

    Task<Void> acceptFriendRequest(String user_id, Friend friend);

    Task<Void> changePassword(String user_id, User user);

    Task<Void> deleteFriendRequest(String user_id, String friend_request_id);
}
