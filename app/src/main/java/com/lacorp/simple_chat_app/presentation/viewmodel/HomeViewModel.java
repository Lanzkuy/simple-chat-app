package com.lacorp.simple_chat_app.presentation.viewmodel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.lacorp.simple_chat_app.domain.entities.Friend;
import com.lacorp.simple_chat_app.domain.entities.User;
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
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.core.SingleObserver;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

@HiltViewModel
public class HomeViewModel extends ViewModel {

    private final MutableLiveData<Resource<List<Friend>>> friends = new MutableLiveData<>();
    private final MutableLiveData<Resource<Boolean>> addFriendStatus = new MutableLiveData<>();
    private final UserUseCase userUseCase;
    private final CompositeDisposable disposable = new CompositeDisposable();

    @Inject
    public HomeViewModel(UserUseCase userUseCase) {
        this.userUseCase = userUseCase;
    }

    public void addFriend(String user_id, String friend_username) {
        userUseCase.addFriend(user_id, friend_username).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new CompletableObserver() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        disposable.add(d);
                        observeAddFriendsStatus().postValue(Resource.Loading(true));
                    }

                    @Override
                    public void onComplete() {
                        observeAddFriendsStatus().postValue(Resource.Success(true));
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        observeAddFriendsStatus().postValue(Resource.Failure(e));
                    }
                });
    }

    public void getFriends(String user_id) {
        userUseCase.getFriends(user_id).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .toObservable()
                .subscribe(new Observer<Resource<List<Friend>>>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        disposable.add(d);
                        observeFriends().postValue(Resource.Loading(new ArrayList<>()));
                    }

                    @Override
                    public void onNext(@NonNull Resource<List<Friend>> friendResource) {
                        assert friendResource.data != null;
                        observeFriends().postValue(Resource.Success(friendResource.data));
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        observeFriends().postValue(Resource.Failure(e));
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    public MutableLiveData<Resource<List<Friend>>> observeFriends() {
        return friends;
    }

    public MutableLiveData<Resource<Boolean>> observeAddFriendsStatus() {
        return addFriendStatus;
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        if(disposable != null) {
            disposable.dispose();
        }
    }
}
