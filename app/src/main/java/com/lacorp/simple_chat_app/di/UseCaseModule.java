package com.lacorp.simple_chat_app.di;

import com.lacorp.simple_chat_app.domain.repository.IAuthRepository;
import com.lacorp.simple_chat_app.domain.usecase.AuthUseCase;
import com.lacorp.simple_chat_app.domain.usecase.validator.ValidateFullname;
import com.lacorp.simple_chat_app.domain.usecase.validator.ValidatePassword;
import com.lacorp.simple_chat_app.domain.usecase.validator.ValidateUsername;
import com.lacorp.simple_chat_app.domain.usecase.validator.ValidatorUseCase;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.components.SingletonComponent;

@Module
@InstallIn(SingletonComponent.class)
public class UseCaseModule {

    @Provides
    @Singleton
    public ValidatorUseCase provideValidatorUseCase() {
        return new ValidatorUseCase(
                new ValidateUsername(),
                new ValidatePassword(),
                new ValidateFullname()
        );
    }

    @Provides
    @Singleton
    public AuthUseCase provideAuthUseCase(IAuthRepository authRepository, ValidatorUseCase validatorUseCase) {
        return new AuthUseCase(authRepository, validatorUseCase);
    }
}
