package com.lacorp.simple_chat_app.data.repository;

import static com.lacorp.simple_chat_app.utils.Constants.USER_COLLECTION;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.lacorp.simple_chat_app.domain.repository.IUserRepository;

public class UserRepository implements IUserRepository {

    private final FirebaseFirestore firestore;

    public UserRepository(FirebaseFirestore firestore) {
        this.firestore = firestore;
    }

    @Override
    public Task<DocumentSnapshot> getUser(String user_id) {
        return firestore.collection(USER_COLLECTION)
                .document(user_id).get();
    }

    @Override
    public Query getFriends(String user_id) {
        return firestore.collection(USER_COLLECTION)
                .whereNotEqualTo("user_id", user_id);
    }
}
