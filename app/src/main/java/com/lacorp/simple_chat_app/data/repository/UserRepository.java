package com.lacorp.simple_chat_app.data.repository;

import static com.lacorp.simple_chat_app.utils.Constants.USER_COLLECTION;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.lacorp.simple_chat_app.data.entities.User;
import com.lacorp.simple_chat_app.domain.repository.IUserRepository;
import com.lacorp.simple_chat_app.utils.Resource;

import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Single;

public class UserRepository implements IUserRepository {

    private final FirebaseFirestore firestore;

    public UserRepository(FirebaseFirestore firestore) {
        this.firestore = firestore;
    }

    @Override
    public Flowable<Resource<List<User>>> getAllUsers() {
        return null;
    }

    @Override
    public Flowable<Resource<List<User>>> getAllUserFriends() {
        return null;
    }

    @Override
    public Single<Resource<User>> login(String username, String password) {
        return Single.create(emitter -> {
            Query querySnapshot = firestore.collection(USER_COLLECTION)
                            .whereEqualTo("username", username)
                            .whereEqualTo("password", password);
            querySnapshot.addSnapshotListener((value, error) -> {
                if(error != null) {
                    emitter.onError(error);
                }

                if(value != null) {
                    if(!value.isEmpty()) {
                        User user = value.toObjects(User.class).get(0);
                        emitter.onSuccess(Resource.Success(user));
                    }
                    else {
                        emitter.onSuccess(Resource.Success(new User()));
                    }
                }
            });
        });
    }

    @Override
    public Completable register(User user) {
        return Completable.create(emitter -> {
            String newId = firestore.collection(USER_COLLECTION).document().getId();
            user.setUser_id(newId);

            firestore.collection(USER_COLLECTION).document(newId).set(user)
                    .addOnSuccessListener(e -> emitter.onComplete())
                    .addOnFailureListener(emitter::onError);
        });
    }
}
