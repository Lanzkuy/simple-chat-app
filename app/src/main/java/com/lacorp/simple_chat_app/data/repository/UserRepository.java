package com.lacorp.simple_chat_app.data.repository;

import com.google.firebase.firestore.FirebaseFirestore;
import com.lacorp.simple_chat_app.data.entities.User;
import com.lacorp.simple_chat_app.domain.repository.IUserRepository;
import com.lacorp.simple_chat_app.utils.Resource;

import java.util.List;

import io.reactivex.rxjava3.core.Flowable;

public class UserRepository implements IUserRepository {

    private final FirebaseFirestore firestore;

    public UserRepository(FirebaseFirestore firestore) {
        this.firestore = firestore;
    }

    @Override
    public Flowable<Resource<List<User>>> getAllUsers() {
        return null;
    }

    @Override
    public Flowable<Resource<List<User>>> getAllUserFriends() {
        return null;
    }
}
