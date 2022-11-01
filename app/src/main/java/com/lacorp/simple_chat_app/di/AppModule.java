package com.lacorp.simple_chat_app.di;

import static com.lacorp.simple_chat_app.utils.Constants.SHARED_PREFERENCE_NAME;

import android.content.Context;
import android.content.SharedPreferences;

import com.lacorp.simple_chat_app.presentation.adapter.ChatRoomAdapter;

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
    public SharedPreferences provideSharedPreference(@ApplicationContext Context context) {
        return context.getSharedPreferences(SHARED_PREFERENCE_NAME, Context.MODE_PRIVATE);
    }
}
