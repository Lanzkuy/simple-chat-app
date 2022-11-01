package com.lacorp.simple_chat_app.di;

import com.google.firebase.firestore.FirebaseFirestore;
import com.lacorp.simple_chat_app.data.repository.AuthRepository;
import com.lacorp.simple_chat_app.data.repository.ChatRepository;
import com.lacorp.simple_chat_app.data.repository.UserRepository;
import com.lacorp.simple_chat_app.domain.repository.IAuthRepository;
import com.lacorp.simple_chat_app.domain.repository.IChatRepository;
import com.lacorp.simple_chat_app.domain.repository.IUserRepository;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.components.SingletonComponent;

@Module
@InstallIn(SingletonComponent.class)
public class RepositoryModule {

    @Provides
    @Singleton
    public IAuthRepository provideAuthRepository(FirebaseFirestore firestore) {
        return new AuthRepository(firestore);
    }

    @Provides
    @Singleton
    public IUserRepository provideUserRepository(FirebaseFirestore firestore) {
        return new UserRepository(firestore);
    }

    @Provides
    @Singleton
    public IChatRepository provideChatRepository(FirebaseFirestore firestore) {
        return new ChatRepository(firestore);
    }
}
