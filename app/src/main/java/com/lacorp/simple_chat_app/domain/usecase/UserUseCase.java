package com.lacorp.simple_chat_app.domain.usecase;

import com.lacorp.simple_chat_app.domain.entities.Friend;
import com.lacorp.simple_chat_app.domain.entities.FriendRequest;
import com.lacorp.simple_chat_app.domain.entities.Message;
import com.lacorp.simple_chat_app.domain.entities.User;
import com.lacorp.simple_chat_app.domain.repository.IUserRepository;
import com.lacorp.simple_chat_app.domain.usecase.validator.ValidatorUseCase;
import com.lacorp.simple_chat_app.utils.Resource;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.core.BackpressureStrategy;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Single;

public class UserUseCase {

    private final IUserRepository userRepository;
    private final ValidatorUseCase validatorUseCase;

    public UserUseCase(IUserRepository userRepository, ValidatorUseCase validatorUseCase) {
        this.userRepository = userRepository;
        this.validatorUseCase = validatorUseCase;
    }

    public ValidatorUseCase getValidatorUseCase() {
        return validatorUseCase;
    }

    public Single<Resource<User>> getUserByID(String user_id) {
        return Single.create(emitter -> userRepository
                .getUserByID(user_id)
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

    public Single<Resource<User>> getUserByUsername(String username) {
        return Single.create(emitter -> userRepository
                .getUserByUsername(username)
                .get().addOnCompleteListener(task -> {
                    if(!task.isSuccessful()) {
                        emitter.onError(new Exception("Something went wrong"));
                        return;
                    }

                    List<User> userList = task.getResult().toObjects(User.class);
                    if(userList.isEmpty()) {
                        emitter.onError(new Exception("User not found"));
                        return;
                    }

                    emitter.onSuccess(Resource.Success(userList.get(0)));
                }));
    }

    public Flowable<Resource<List<Friend>>> getFriends(String user_id) {
        return Flowable.create(emitter -> userRepository
                .getFriends(user_id)
                .addSnapshotListener((value, error) -> {
                    if(error != null) {
                        emitter.onError(new Exception("Something went wrong"));
                        return;
                    }

                    if (value == null) {
                        emitter.onNext(Resource.Success(new ArrayList<>()));
                        return;
                    }

                    List<Friend> friends = value.toObjects(Friend.class);
                    emitter.onNext(Resource.Success(friends));
                }), BackpressureStrategy.BUFFER);
//                .get().addOnCompleteListener(task -> {
//                    if(!task.isSuccessful()) {
//                        emitter.onError(new Exception("Something went wrong"));
//                        return;
//                    }
//
//                    List<Friend> friends = task.getResult().toObjects(Friend.class);
//                    if(friends.isEmpty()) {
//                        emitter.onNext(Resource.Success(new ArrayList<>()));
//                        return;
//                    }
//
//                    emitter.onNext(Resource.Success(friends));
//                }), BackpressureStrategy.BUFFER);
    }

    public Flowable<Resource<List<FriendRequest>>> getFriendRequests(String user_id) {
        return Flowable.create(emitter -> userRepository
                .getFriendRequests(user_id)
                .get().addOnCompleteListener(task -> {
                    if(!task.isSuccessful()) {
                        emitter.onError(new Exception("Something went wrong"));
                        return;
                    }

                    List<FriendRequest> friendRequests = task.getResult().toObjects(FriendRequest.class);
                    if(friendRequests.isEmpty()) {
                        emitter.onNext(Resource.Success(new ArrayList<>()));
                        return;
                    }

                    emitter.onNext(Resource.Success(friendRequests));
                }), BackpressureStrategy.BUFFER);
    }

    public Completable addFriend(String user_id, String friend_username) {
        return Completable.create(emitter -> {
            User userRequest = getUserByID(user_id).blockingGet().data;
            User userResponse = getUserByUsername(friend_username).blockingGet().data;

            assert userRequest != null;
            assert userResponse != null;

            if(userRequest.getUser_id().equals(userResponse.getUser_id())) {
                emitter.onError(new Exception("You can't add yourself"));
                return;
            }

            userRepository.addFriend(userResponse.getUser_id(),
                            new FriendRequest("",
                                    userRequest.getUser_id(),
                                    userRequest.getUsername()))
                    .addOnSuccessListener(e -> emitter.onComplete())
                    .addOnFailureListener(emitter::onError);
        });
    }

    public Completable acceptFriendRequest(String user_id, String friend_id) {
        return Completable.create(emitter -> {
            User friend = getUserByID(friend_id).blockingGet().data;

            assert friend != null;

            userRepository.acceptFriendRequest(user_id,
                            new Friend("",
                            friend.getUser_id(),
                            friend.getUsername(),
                            friend.getFullname()))
                    .addOnSuccessListener(e -> emitter.onComplete())
                    .addOnFailureListener(emitter::onError);
        });
    }

    public Completable changePassword(String user_id, String oldPassword, String newPassword) {
        return Completable.create(emitter -> {
            User user = getUserByID(user_id).blockingGet().data;

            assert user != null;

            if(!user.getPassword().equals(oldPassword)) {
                emitter.onError(new Exception("Password was wrong"));
                return;
            }

            if(user.getPassword().equals(newPassword)) {
                emitter.onError(new Exception("New password can't be the same as the old password"));
                return;
            }

            user.setPassword(newPassword);

            userRepository.changePassword(user_id, user)
                .addOnSuccessListener(e -> emitter.onComplete())
                .addOnFailureListener(emitter::onError);
        });
    }

    public Completable deleteFriendRequest(String user_id, String friend_request_id) {
        return Completable.create(emitter -> userRepository
                .deleteFriendRequest(user_id, friend_request_id)
                .addOnSuccessListener(e -> emitter.onComplete())
                .addOnFailureListener(emitter::onError));
    }
}
