package com.lacorp.simple_chat_app.domain.repository;

import androidx.lifecycle.MutableLiveData;

import com.lacorp.simple_chat_app.data.entities.User;
import com.lacorp.simple_chat_app.utils.Resource;

import java.util.List;

import io.reactivex.rxjava3.core.Flowable;

public interface IUserRepository {

    Resource<MutableLiveData<List<User>>> getAllUsers();

    Resource<MutableLiveData<List<User>>> getAllUserFriends();

    Flowable<Resource<User>> signIn(String username, String password);

    Resource<Boolean> signUp(User user);
}
