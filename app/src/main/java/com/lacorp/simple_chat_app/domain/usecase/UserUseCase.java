package com.lacorp.simple_chat_app.domain.usecase;

import com.lacorp.simple_chat_app.domain.entities.User;
import com.lacorp.simple_chat_app.domain.repository.IUserRepository;
import com.lacorp.simple_chat_app.domain.usecase.validator.ValidatorUseCase;
import com.lacorp.simple_chat_app.utils.Resource;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.core.BackpressureStrategy;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Single;

public class UserUseCase {

    private final IUserRepository userRepository;

    public UserUseCase(IUserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Single<Resource<User>> getUser(String user_id) {
        return Single.create(emitter -> userRepository.getUser(user_id)
                .addOnCompleteListener(task -> {
                    if(!task.isSuccessful()) {
                        emitter.onError(new Exception("Something went wrong"));
                        return;
                    }

                    User user = task.getResult().toObject(User.class);
                    if(user == null) {
                        emitter.onError(new Exception("User not found"));
                        return;
                    }

                    emitter.onSuccess(Resource.Success(user));
                }));
    }

    public Flowable<Resource<List<User>>> getFriends(String user_id) {
        return Flowable.create(emitter -> userRepository.getFriends(user_id)
                .get().addOnCompleteListener(task -> {
                    if(!task.isSuccessful()) {
                        emitter.onError(new Exception("Something went wrong"));
                        return;
                    }

                    List<User> user = task.getResult().toObjects(User.class);
                    if(user.isEmpty()) {
                        emitter.onNext(Resource.Success(new ArrayList<>()));
                        return;
                    }

                    emitter.onNext(Resource.Success(user));
                }), BackpressureStrategy.BUFFER);
    }
}
