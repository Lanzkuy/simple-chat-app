package com.lacorp.simple_chat_app.domain.usecase;

import com.lacorp.simple_chat_app.data.entities.Message;
import com.lacorp.simple_chat_app.domain.repository.IChatRepository;
import com.lacorp.simple_chat_app.domain.usecase.validator.ValidatorUseCase;
import com.lacorp.simple_chat_app.utils.Resource;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.core.BackpressureStrategy;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Flowable;

public class ChatUseCase {

    private final IChatRepository chatRepository;
    private final ValidatorUseCase validatorUseCase;

    public ChatUseCase(IChatRepository chatRepository, ValidatorUseCase validatorUseCase) {
        this.chatRepository = chatRepository;
        this.validatorUseCase = validatorUseCase;
    }

    public ValidatorUseCase getValidatorUseCase() {
        return validatorUseCase;
    }

    public Flowable<Resource<List<Message>>> getMessages(String user_id, String friend_id) {
        return Flowable.create(emitter -> chatRepository.getMessages(user_id, friend_id)
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
                }), BackpressureStrategy.BUFFER);
    }

    public Completable sendMessage(Message message, String friend_id) {
        return Completable.create(emitter -> chatRepository.sendMessage(message,friend_id)
                .addOnSuccessListener(e -> emitter.onComplete())
                .addOnFailureListener(emitter::onError));
    }
}
