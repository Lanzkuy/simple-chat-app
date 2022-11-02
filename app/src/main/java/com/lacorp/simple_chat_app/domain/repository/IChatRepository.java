package com.lacorp.simple_chat_app.domain.repository;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.Query;
import com.lacorp.simple_chat_app.data.entities.Message;

public interface IChatRepository {

    Query getMessages(String user, String friend);

    Task<Void> sendMessage(Message message, String friend_id);
}
