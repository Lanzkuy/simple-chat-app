package com.lacorp.simple_chat_app.presentation.viewmodel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.lacorp.simple_chat_app.domain.entities.Friend;
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
public class HomeViewModel extends ViewModel {

    private final MutableLiveData<Resource<List<Friend>>> friendsState = new MutableLiveData<>();
    private final MutableLiveData<Resource<Boolean>> addFriendState = new MutableLiveData<>();
    private final UserUseCase userUseCase;
    private final CompositeDisposable disposable = new CompositeDisposable();

    @Inject
    public HomeViewModel(UserUseCase userUseCase) {
        this.userUseCase = userUseCase;
    }

    public void addFriend(String user_id, String friend_username) {
        userUseCase.addFriend(user_id, friend_username)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new CompletableObserver() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        disposable.add(d);
                        observeAddFriendsState().postValue(Resource.Loading(true));
                    }

                    @Override
                    public void onComplete() {
                        observeAddFriendsState().postValue(Resource.Success(true));
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        observeAddFriendsState().postValue(Resource.Failure(e));
                    }
                });
    }

    public void getFriends(String user_id) {
        userUseCase.getFriends(user_id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .toObservable()
                .subscribe(new Observer<Resource<List<Friend>>>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        disposable.add(d);
                        observeFriendsState().postValue(Resource.Loading(new ArrayList<>()));
                    }

                    @Override
                    public void onNext(@NonNull Resource<List<Friend>> friendResource) {
                        assert friendResource.data != null;
                        observeFriendsState().postValue(Resource.Success(friendResource.data));
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        observeFriendsState().postValue(Resource.Failure(e));
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    public MutableLiveData<Resource<List<Friend>>> observeFriendsState() {
        return friendsState;
    }

    public MutableLiveData<Resource<Boolean>> observeAddFriendsState() {
        return addFriendState;
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        if(disposable != null) {
            disposable.dispose();
        }
    }
}
