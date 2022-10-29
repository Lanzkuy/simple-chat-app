package com.lacorp.simple_chat_app.domain.usecase;

import com.lacorp.simple_chat_app.data.entities.User;
import com.lacorp.simple_chat_app.domain.repository.IUserRepository;
import com.lacorp.simple_chat_app.utils.Resource;

import javax.inject.Inject;

import io.reactivex.rxjava3.core.Flowable;

public class LoginUseCase {

    private final IUserRepository userRepository;

    @Inject
    public LoginUseCase(IUserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Flowable<Resource<User>> invoke(String username, String password) {
        return userRepository.signIn(username, password);
    }
}
