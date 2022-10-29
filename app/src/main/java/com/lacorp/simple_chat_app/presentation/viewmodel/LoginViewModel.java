package com.lacorp.simple_chat_app.presentation.viewmodel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.lacorp.simple_chat_app.data.entities.User;
import com.lacorp.simple_chat_app.domain.usecase.LoginUseCase;
import com.lacorp.simple_chat_app.utils.Resource;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;
@HiltViewModel
public class LoginViewModel extends ViewModel {

    private final LoginUseCase loginUseCase;
    public MutableLiveData<Resource<User>> loggedInUser = new MutableLiveData<>();
    private CompositeDisposable disposable = new CompositeDisposable();

    @Inject
    public LoginViewModel(LoginUseCase loginUseCase) {
        this.loginUseCase = loginUseCase;
    }

    public void loginUser(String username, String password) {
        loggedInUser.postValue(Resource.Loading(new User()));
        loginUseCase.invoke(username, password).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .toObservable()
                .subscribe(new Observer<Resource<User>>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        disposable.add(d);
                    }

                    @Override
                    public void onNext(@NonNull Resource<User> userResource) {
                        assert userResource.data != null;
                        loggedInUser.postValue(Resource.Success(userResource.data));
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        loggedInUser.postValue(Resource.Failure(e));
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }
}
