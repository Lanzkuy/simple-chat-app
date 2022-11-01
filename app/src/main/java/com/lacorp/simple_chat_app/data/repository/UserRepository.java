package com.lacorp.simple_chat_app.data.repository;

import static com.lacorp.simple_chat_app.utils.Constants.USER_COLLECTION;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.lacorp.simple_chat_app.data.entities.User;
import com.lacorp.simple_chat_app.domain.repository.IUserRepository;
import com.lacorp.simple_chat_app.utils.Resource;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.core.BackpressureStrategy;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.FlowableEmitter;
import io.reactivex.rxjava3.core.FlowableOnSubscribe;
import io.reactivex.rxjava3.core.Single;

public class UserRepository implements IUserRepository {

    private final FirebaseFirestore firestore;

    public UserRepository(FirebaseFirestore firestore) {
        this.firestore = firestore;
    }

    @Override
    public Single<Resource<User>> getUser(String user_id) {
        return Single.create(emitter -> firestore.collection(USER_COLLECTION).document(user_id)
                .get().addOnCompleteListener(task -> {
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

    @Override
    public Flowable<Resource<List<User>>> getFriends(String user_id) {
        return Flowable.create(emitter -> firestore.collection(USER_COLLECTION)
                .whereNotEqualTo("user_id", user_id)
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
