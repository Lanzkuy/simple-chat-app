package com.lacorp.simple_chat_app.domain.repository;

import com.lacorp.simple_chat_app.data.entities.User;
import com.lacorp.simple_chat_app.utils.Resource;

import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Single;

public interface IUserRepository {

    Flowable<Resource<List<User>>> getAllUsers();

    Flowable<Resource<List<User>>> getAllUserFriends();

    Single<Resource<User>> login(String username, String password);

    Completable register(User user);
}
