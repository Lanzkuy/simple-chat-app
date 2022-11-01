package com.lacorp.simple_chat_app.domain.repository;

import com.lacorp.simple_chat_app.data.entities.User;
import com.lacorp.simple_chat_app.utils.Resource;

import java.util.List;

import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Single;

public interface IUserRepository {

    Single<Resource<User>> getUser(String user_id);

    Flowable<Resource<List<User>>> getFriends(String user_id);
}
