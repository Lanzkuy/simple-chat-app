package com.lacorp.simple_chat_app.di;

import com.google.firebase.firestore.FirebaseFirestore;
import com.lacorp.simple_chat_app.data.repository.UserRepository;
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
    public IUserRepository provideUserRepository(FirebaseFirestore firestore) {
        return new UserRepository(firestore);
    }
}
