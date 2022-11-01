package com.lacorp.simple_chat_app.data.repository;

import static com.lacorp.simple_chat_app.utils.Constants.CHATS_COLLECTION;
import static com.lacorp.simple_chat_app.utils.Constants.MESSAGES_COLLECTION;

import com.google.firebase.firestore.FirebaseFirestore;
import com.lacorp.simple_chat_app.data.entities.Message;
import com.lacorp.simple_chat_app.domain.repository.IChatRepository;
import com.lacorp.simple_chat_app.utils.Resource;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.core.BackpressureStrategy;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Flowable;

public class ChatRepository implements IChatRepository {

    private final FirebaseFirestore firestore;

    public ChatRepository(FirebaseFirestore firestore) {
        this.firestore = firestore;
    }

    @Override
    public Flowable<Resource<List<Message>>> getMessages(String user_id, String friend_id) {
        return Flowable.create(emitter -> {
            firestore.collection(CHATS_COLLECTION)
                    .document(user_id+friend_id)
                    .collection("messages")
                    .addSnapshotListener((value, error) -> {
                        if(error != null) {
                            emitter.onError(new Exception("Something went wrong"));
                            return;
                        }

                        if (value == null) {
                            emitter.onNext(Resource.Success(new ArrayList<>()));
                            return;
                        }

                        List<Message> messageList = value.toObjects(Message.class);
                        emitter.onNext(Resource.Success(messageList));
                    });
        }, BackpressureStrategy.BUFFER);
    }

    @Override
    public Completable sendMessage(Message message, String friend_id) {
        return Completable.create(emitter -> {
            firestore.collection(CHATS_COLLECTION)
                    .document(message.getSender_id()+friend_id)
                    .collection(MESSAGES_COLLECTION).document()
                    .set(message)
                    .addOnSuccessListener(e -> {
                        emitter.onComplete();
                    })
                    .addOnFailureListener(emitter::onError);
        });
    }
}
