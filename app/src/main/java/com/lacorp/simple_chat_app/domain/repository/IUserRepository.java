package com.lacorp.simple_chat_app.domain.repository;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.Query;
import com.lacorp.simple_chat_app.domain.entities.Friend;
import com.lacorp.simple_chat_app.domain.entities.FriendRequest;

public interface IUserRepository {

    DocumentReference getUserByID(String user_id);

    Query getUserByUsername(String username);

    Task<Void> addFriend(String user_id, FriendRequest friendRequest);

    Query getFriends(String user_id);

    Task<Void> acceptFriendRequest(String user_id, Friend friend);

    Query getFriendRequests(String user_id);

    Task<Void> deleteFriendRequest(String user_id, String friend_request_id);
}
