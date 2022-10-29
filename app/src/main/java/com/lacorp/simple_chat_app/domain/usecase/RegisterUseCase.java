package com.lacorp.simple_chat_app.domain.usecase;

import com.lacorp.simple_chat_app.data.entities.User;
import com.lacorp.simple_chat_app.domain.repository.IUserRepository;

import io.reactivex.rxjava3.core.Completable;

public class RegisterUseCase {

    private final IUserRepository userRepository;

    public RegisterUseCase(IUserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Completable invoke(User user) {
        return userRepository.register(user);
    }
}
