package com.lacorp.simple_chat_app.data.repository;

import static com.lacorp.simple_chat_app.utils.Constants.CHATS_COLLECTION;
import static com.lacorp.simple_chat_app.utils.Constants.MESSAGES_COLLECTION;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.lacorp.simple_chat_app.data.entities.Message;
import com.lacorp.simple_chat_app.domain.repository.IChatRepository;

public class ChatRepository implements IChatRepository {

    private final FirebaseFirestore firestore;

    public ChatRepository(FirebaseFirestore firestore) {
        this.firestore = firestore;
    }

    @Override
    public Query getMessages(String user_id, String friend_id) {
        return firestore.collection(CHATS_COLLECTION)
                .document(user_id + friend_id)
                .collection(MESSAGES_COLLECTION).orderBy("last_message_time");
    }

    @Override
    public Task<Void> sendMessage(Message message, String friend_id) {
        return firestore.collection(CHATS_COLLECTION)
                .document(message.getSender_id() + friend_id)
                .collection(MESSAGES_COLLECTION).document().set(message);
    }
}
