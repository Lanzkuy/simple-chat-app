package com.lacorp.simple_chat_app.data.repository;

import static com.lacorp.simple_chat_app.utils.Constants.FRIEND_COLLECTION;
import static com.lacorp.simple_chat_app.utils.Constants.FRIEND_REQUEST_COLLECTION;
import static com.lacorp.simple_chat_app.utils.Constants.USER_COLLECTION;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.lacorp.simple_chat_app.domain.entities.Friend;
import com.lacorp.simple_chat_app.domain.entities.FriendRequest;
import com.lacorp.simple_chat_app.domain.entities.User;
import com.lacorp.simple_chat_app.domain.repository.IUserRepository;

public class UserRepository implements IUserRepository {

    private final FirebaseFirestore firestore;

    public UserRepository(FirebaseFirestore firestore) {
        this.firestore = firestore;
    }

    @Override
    public DocumentReference getUserByID(String user_id) {
        return firestore.collection(USER_COLLECTION)
                .document(user_id);
    }

    @Override
    public Query getUserByUsername(String username) {
        return firestore.collection(USER_COLLECTION)
                .whereEqualTo("username", username);
    }

    @Override
    public Query getFriends(String user_id) {
        return firestore.collection(USER_COLLECTION)
                .document(user_id)
                .collection(FRIEND_COLLECTION);
    }

    @Override
    public Query getFriendRequests(String user_id) {
        return firestore.collection(USER_COLLECTION)
                .document(user_id)
                .collection(FRIEND_REQUEST_COLLECTION);
    }

    @Override
    public Task<Void> addFriend(String user_id, FriendRequest friendRequest) {
        String newId = firestore.collection(USER_COLLECTION)
                .document(user_id)
                .collection(FRIEND_REQUEST_COLLECTION)
                .document().getId();
        friendRequest.setFriend_request_id(newId);

        return firestore.collection(USER_COLLECTION)
                .document(user_id)
                .collection(FRIEND_REQUEST_COLLECTION)
                .document(newId)
                .set(friendRequest);
    }

    @Override
    public Task<Void> acceptFriendRequest(String user_id, Friend friend) {
        String newId = firestore.collection(USER_COLLECTION)
                .document(user_id)
                .collection(FRIEND_COLLECTION)
                .document().getId();
        friend.setFriend_id(newId);

        return firestore.collection(USER_COLLECTION)
                .document(user_id)
                .collection(FRIEND_COLLECTION)
                .document(newId)
                .set(friend);
    }

    @Override
    public Task<Void> changePassword(String user_id, User user) {
        return firestore.collection(USER_COLLECTION)
                .document(user_id)
                .set(user);
    }

    @Override
    public Task<Void> deleteFriendRequest(String user_id, String friend_request_id) {
        return firestore.collection(USER_COLLECTION)
                .document(user_id)
                .collection(FRIEND_REQUEST_COLLECTION)
                .document(friend_request_id)
                .delete();
    }
}
