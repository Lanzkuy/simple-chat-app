package com.lacorp.simple_chat_app.data.repository;

import static com.lacorp.simple_chat_app.utils.Constants.USER_COLLECTION;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.lacorp.simple_chat_app.data.entities.User;
import com.lacorp.simple_chat_app.domain.repository.IAuthRepository;
import com.lacorp.simple_chat_app.utils.Resource;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Single;

public class AuthRepository implements IAuthRepository {

    private final FirebaseFirestore firestore;

    public AuthRepository(FirebaseFirestore firestore) {
        this.firestore = firestore;
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
                        emitter.onError(new IllegalArgumentException("Username or password was wrong"));
                    }
                }
            });
        });
    }

    @Override
    public Completable register(User user) {
        return Completable.create(emitter -> {
            boolean isAvailable = checkUsername(user.getUsername())
                    .blockingGet();

            if(!isAvailable) {
                emitter.onError(new Exception("Username already exists"));
                return;
            }

            String newId = firestore.collection(USER_COLLECTION).document().getId();
            user.setUser_id(newId);

            firestore.collection(USER_COLLECTION).document(newId).set(user)
                    .addOnSuccessListener(e -> emitter.onComplete())
                    .addOnFailureListener(emitter::onError);
        });
    }

    @Override
    public Single<Boolean> checkUsername(String username) {
        return Single.create(emitter -> firestore.collection(USER_COLLECTION)
                .whereEqualTo("username", username)
                .get().addOnSuccessListener(queryDocumentSnapshots -> {
                    if(queryDocumentSnapshots.isEmpty()) {
                        emitter.onSuccess(true);
                    }
                    else {
                        emitter.onSuccess(false);
                    }
                }));
    }
}
