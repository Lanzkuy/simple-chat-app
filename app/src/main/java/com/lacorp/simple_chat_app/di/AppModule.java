package com.lacorp.simple_chat_app.di;

import static com.lacorp.simple_chat_app.utils.Constants.SHARED_PREFERENCE_NAME;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.firebase.firestore.FirebaseFirestore;
import com.lacorp.simple_chat_app.data.repository.UserRepository;
import com.lacorp.simple_chat_app.domain.repository.IUserRepository;
import com.lacorp.simple_chat_app.domain.usecase.LoginUseCase;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.android.qualifiers.ApplicationContext;
import dagger.hilt.components.SingletonComponent;

@Module
@InstallIn(SingletonComponent.class)
public class AppModule {

    @Provides
    @Singleton
    public FirebaseFirestore provideFirestore() {
        return FirebaseFirestore.getInstance();
    }

    @Provides
    @Singleton
    public SharedPreferences provideSharedPreference(@ApplicationContext Context context) {
        return context.getSharedPreferences(SHARED_PREFERENCE_NAME, Context.MODE_PRIVATE);
    }

    @Provides
    @Singleton
    public IUserRepository provideUserRepository(FirebaseFirestore firestore, SharedPreferences sharedPreferences) {
        return new UserRepository(firestore, sharedPreferences);
    }

    @Provides
    @Singleton
    public LoginUseCase provideLoginUseCase(IUserRepository userRepository) {
        return new LoginUseCase(userRepository);
    }
}
