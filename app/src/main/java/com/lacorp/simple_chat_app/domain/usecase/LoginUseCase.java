package com.lacorp.simple_chat_app.domain.usecase;

import com.lacorp.simple_chat_app.data.entities.User;
import com.lacorp.simple_chat_app.domain.repository.IUserRepository;
import com.lacorp.simple_chat_app.utils.Resource;

import io.reactivex.rxjava3.core.Single;

public class LoginUseCase {

    private final IUserRepository userRepository;

    public LoginUseCase(IUserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Single<Resource<User>> invoke(String username, String password) {
        return userRepository.login(username, password);
    }
}
