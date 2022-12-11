package com.lacorp.simple_chat_app.presentation.viewmodel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.lacorp.simple_chat_app.domain.entities.FriendRequest;
import com.lacorp.simple_chat_app.domain.usecase.UserUseCase;
import com.lacorp.simple_chat_app.utils.Resource;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.CompletableObserver;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

@HiltViewModel
public class FriendRequestViewModel extends ViewModel {

    private final MutableLiveData<Resource<List<FriendRequest>>> friendRequestsState = new MutableLiveData<>();
    private final MutableLiveData<Resource<FriendRequest>> acceptRequestState = new MutableLiveData<>();
    private final MutableLiveData<Resource<Boolean>> deleteRequestState = new MutableLiveData<>();
    private final UserUseCase userUseCase;
    private final CompositeDisposable disposable = new CompositeDisposable();

    @Inject
    public FriendRequestViewModel(UserUseCase userUseCase) {
        this.userUseCase = userUseCase;
    }

    public void acceptFriendRequest(String user_id, FriendRequest friendRequest) {
        userUseCase.acceptFriendRequest(user_id, friendRequest.getUser_id())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new CompletableObserver() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        disposable.add(d);
                        observeAcceptRequestState().postValue(Resource.Loading(friendRequest));
                    }

                    @Override
                    public void onComplete() {
                        userUseCase.acceptFriendRequest(friendRequest.getUser_id(), user_id)
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe();
                        observeAcceptRequestState().postValue(Resource.Success(friendRequest));
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        observeAcceptRequestState().postValue(Resource.Failure(e));
                    }
                });
    }

    public void getFriendRequests(String user_id) {
        userUseCase.getFriendRequests(user_id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .toObservable()
                .subscribe(new Observer<Resource<List<FriendRequest>>>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        disposable.add(d);
                        observeFriendRequestsState().postValue(Resource.Loading(new ArrayList<>()));
                    }

                    @Override
                    public void onNext(@NonNull Resource<List<FriendRequest>> friendRequestResource) {
                        assert friendRequestResource.data != null;
                        observeFriendRequestsState().postValue(Resource.Success(friendRequestResource.data));
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        observeFriendRequestsState().postValue(Resource.Failure(e));
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    public void deleteFriendRequest(String user_id, String friend_request_id) {
        userUseCase.deleteFriendRequest(user_id, friend_request_id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new CompletableObserver() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        disposable.add(d);
                        observeDeleteRequestState().postValue(Resource.Loading(true));
                    }

                    @Override
                    public void onComplete() {
                        observeDeleteRequestState().postValue(Resource.Success(true));
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        observeDeleteRequestState().postValue(Resource.Failure(e));
                    }
                });
    }

    public MutableLiveData<Resource<List<FriendRequest>>> observeFriendRequestsState() {
        return friendRequestsState;
    }

    public MutableLiveData<Resource<FriendRequest>> observeAcceptRequestState() {
        return acceptRequestState;
    }

    public MutableLiveData<Resource<Boolean>> observeDeleteRequestState() {
        return deleteRequestState;
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        if(disposable != null) {
            disposable.dispose();
        }
    }
}
