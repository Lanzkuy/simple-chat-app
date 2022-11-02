package com.lacorp.simple_chat_app.domain.usecase;

import com.lacorp.simple_chat_app.data.entities.User;
import com.lacorp.simple_chat_app.domain.repository.IAuthRepository;
import com.lacorp.simple_chat_app.domain.usecase.validator.ValidatorUseCase;
import com.lacorp.simple_chat_app.utils.Resource;

import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Single;

public class AuthUseCase {

    private final IAuthRepository authRepository;
    private final ValidatorUseCase validatorUseCase;

    public AuthUseCase(IAuthRepository authRepository, ValidatorUseCase validatorUseCase) {
        this.authRepository = authRepository;
        this.validatorUseCase = validatorUseCase;
    }

    public ValidatorUseCase getValidatorUseCase() {
        return validatorUseCase;
    }

    public Single<Resource<User>> login(String username, String password) {
        return Single.create(emitter -> authRepository.login(username, password)
                .get().addOnCompleteListener(task -> {
            if(!task.isSuccessful()) {
                emitter.onError(new Exception("Something went wrong"));
                return;
            }

            List<User> userList = task.getResult().toObjects(User.class);
            if(userList.isEmpty()) {
                emitter.onError(new Exception("Username or password was wrong"));
                return;
            }

            emitter.onSuccess(Resource.Success(userList.get(0)));
        }));
    }

    public Completable register(User user) {
        return Completable.create(emitter -> {
            boolean isAvailable = checkUsername(user.getUsername())
                    .blockingGet();

            if(!isAvailable) {
                emitter.onError(new Exception("Username already exists"));
                return;
            }

            authRepository.register(user)
                    .addOnSuccessListener(e -> emitter.onComplete())
                    .addOnFailureListener(emitter::onError);
        });
    }

    public Single<Boolean> checkUsername(String username) {
        return Single.create(emitter -> authRepository.checkUsername(username)
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
