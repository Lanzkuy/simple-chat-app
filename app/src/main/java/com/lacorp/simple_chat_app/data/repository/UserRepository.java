package com.lacorp.simple_chat_app.data.repository;

import android.content.SharedPreferences;

import androidx.lifecycle.MutableLiveData;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Query;
import com.lacorp.simple_chat_app.data.entities.User;
import com.lacorp.simple_chat_app.domain.repository.IUserRepository;
import com.lacorp.simple_chat_app.utils.Resource;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.rxjava3.core.BackpressureStrategy;
import io.reactivex.rxjava3.core.Flowable;

public class UserRepository implements IUserRepository {

    private final FirebaseFirestore firestore;
    private final SharedPreferences sharedPreferences;

    @Inject
    public UserRepository(FirebaseFirestore firestore, SharedPreferences sharedPreferences) {
        this.firestore = firestore;
        this.sharedPreferences = sharedPreferences;
    }

    @Override
    public Resource<MutableLiveData<List<User>>> getAllUsers() {
        return null;
    }

    @Override
    public Resource<MutableLiveData<List<User>>> getAllUserFriends() {
        return null;
    }

    @Override
    public Flowable<Resource<User>> signIn(String username, String password) {
        return Flowable.create(emitter -> {
            Query querySnapshot = firestore.collection("users")
                            .whereEqualTo("username", username)
                            .whereEqualTo("password", password);
            final ListenerRegistration registration = querySnapshot.addSnapshotListener((value, error) -> {
                if(error != null) {
                    emitter.onError(error);
                }

                if(value != null) {
                    if(!value.isEmpty()) {
                        User user = value.toObjects(User.class).get(0);
                        emitter.onNext(Resource.Success(user));
                    }
                    else {
                        emitter.onNext(Resource.Success(new User()));
                    }
                }
            });

            emitter.setCancellable(registration::remove);
        }, BackpressureStrategy.BUFFER);
    }

    @Override
    public Resource<Boolean> signUp(User user) {
        return null;
    }
}
