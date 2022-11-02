package com.lacorp.simple_chat_app.domain.repository;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Query;

public interface IUserRepository {

    Task<DocumentSnapshot> getUser(String user_id);

    Query getFriends(String user_id);
}
