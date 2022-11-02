package com.lacorp.simple_chat_app.data.repository;

import static com.lacorp.simple_chat_app.utils.Constants.USER_COLLECTION;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.lacorp.simple_chat_app.data.entities.User;
import com.lacorp.simple_chat_app.domain.repository.IAuthRepository;

public class AuthRepository implements IAuthRepository {

    private final FirebaseFirestore firestore;

    public AuthRepository(FirebaseFirestore firestore) {
        this.firestore = firestore;
    }

    @Override
    public Query login(String username, String password) {
        return firestore.collection(USER_COLLECTION)
                .whereEqualTo("username", username)
                .whereEqualTo("password", password);
    }

    @Override
    public Task<Void> register(User user) {
        String newId = firestore.collection(USER_COLLECTION).document().getId();
        user.setUser_id(newId);
        return firestore.collection(USER_COLLECTION).document(newId).set(user);
    }

    @Override
    public Query checkUsername(String username) {
        return firestore.collection(USER_COLLECTION)
                .whereEqualTo("username", username);
    }
}
