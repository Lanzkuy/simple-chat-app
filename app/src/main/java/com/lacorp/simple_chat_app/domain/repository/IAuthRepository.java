package com.lacorp.simple_chat_app.domain.repository;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.Query;
import com.lacorp.simple_chat_app.domain.entities.User;

public interface IAuthRepository {

    Query login(String username, String password);

    Task<Void> register(User user);

    Query checkUsername(String username);
}
