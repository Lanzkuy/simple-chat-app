package com.lacorp.simple_chat_app.di;

import com.lacorp.simple_chat_app.domain.repository.IUserRepository;
import com.lacorp.simple_chat_app.domain.usecase.LoginUseCase;
import com.lacorp.simple_chat_app.domain.usecase.RegisterUseCase;

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
    public LoginUseCase provideLoginUseCase(IUserRepository userRepository) {
        return new LoginUseCase(userRepository);
    }

    @Provides
    @Singleton
    public RegisterUseCase provideRegisterUseCase(IUserRepository userRepository) {
        return new RegisterUseCase(userRepository);
    }
}
