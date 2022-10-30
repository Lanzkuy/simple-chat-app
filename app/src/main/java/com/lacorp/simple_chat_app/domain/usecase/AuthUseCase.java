package com.lacorp.simple_chat_app.domain.usecase;

import com.lacorp.simple_chat_app.data.entities.User;
import com.lacorp.simple_chat_app.domain.repository.IAuthRepository;
import com.lacorp.simple_chat_app.domain.usecase.validator.ValidatorUseCase;
import com.lacorp.simple_chat_app.utils.Resource;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Single;

public class AuthUseCase {

    private final IAuthRepository authRepository;
    private final ValidatorUseCase validatorUseCase;

    public AuthUseCase(IAuthRepository authRepository, ValidatorUseCase validatorUseCase) {
        this.authRepository = authRepository;
        this.validatorUseCase = validatorUseCase;
    }

    public ValidatorUseCase getValidatorUseCase() {
        return validatorUseCase;
    }

    public Single<Resource<User>> login(String username, String password) {
        return authRepository.login(username, password);
    }

    public Completable register(User user) {
        return authRepository.register(user);
    }
}
