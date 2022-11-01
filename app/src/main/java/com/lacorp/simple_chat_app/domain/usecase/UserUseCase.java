package com.lacorp.simple_chat_app.domain.usecase;

import com.lacorp.simple_chat_app.data.entities.User;
import com.lacorp.simple_chat_app.domain.repository.IUserRepository;
import com.lacorp.simple_chat_app.domain.usecase.validator.ValidatorUseCase;
import com.lacorp.simple_chat_app.utils.Resource;

import java.util.List;

import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Single;

public class UserUseCase {

    private final IUserRepository userRepository;
    private final ValidatorUseCase validatorUseCase;

    public UserUseCase(IUserRepository userRepository, ValidatorUseCase validatorUseCase) {
        this.userRepository = userRepository;
        this.validatorUseCase = validatorUseCase;
    }

    public ValidatorUseCase getValidatorUseCase() {
        return validatorUseCase;
    }

    public Single<Resource<User>> getUser(String user_id) {
        return userRepository.getUser(user_id);
    }

    public Flowable<Resource<List<User>>> getFriends(String user_id) {
        return userRepository.getFriends(user_id);
    }
}
