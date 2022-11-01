package com.lacorp.simple_chat_app.domain.usecase;

import com.lacorp.simple_chat_app.data.entities.Message;
import com.lacorp.simple_chat_app.domain.repository.IChatRepository;
import com.lacorp.simple_chat_app.domain.usecase.validator.ValidatorUseCase;
import com.lacorp.simple_chat_app.utils.Resource;

import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Single;

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
        return chatRepository.getMessages(user_id, friend_id);
    }

    public Completable sendMessage(Message message, String friend_id) {
        return chatRepository.sendMessage(message, friend_id);
    }
}
