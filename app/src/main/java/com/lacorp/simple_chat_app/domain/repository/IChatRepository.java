package com.lacorp.simple_chat_app.domain.repository;

import com.lacorp.simple_chat_app.data.entities.Message;
import com.lacorp.simple_chat_app.utils.Resource;

import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Flowable;

public interface IChatRepository {

    Flowable<Resource<List<Message>>> getMessages(String user, String friend);

    Completable sendMessage(Message message, String friend_id);
}
