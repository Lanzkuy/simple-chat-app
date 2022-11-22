package com.lacorp.simple_chat_app.presentation.viewmodel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.lacorp.simple_chat_app.domain.entities.User;
import com.lacorp.simple_chat_app.domain.usecase.AuthUseCase;
import com.lacorp.simple_chat_app.domain.usecase.validator.ValidationResult;
import com.lacorp.simple_chat_app.utils.Resource;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.SingleObserver;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

@HiltViewModel
public class LoginViewModel extends ViewModel {

    private final MutableLiveData<Resource<User>> loginUser = new MutableLiveData<>();
    private final AuthUseCase authUseCase;
    private final CompositeDisposable disposable = new CompositeDisposable();

    @Inject
    public LoginViewModel(AuthUseCase authUseCase) {
        this.authUseCase = authUseCase;
    }

    public void loginUser(String username, String password) {
        ValidationResult usernameValidation= authUseCase
                .getValidatorUseCase()
                .getValidateUsername()
                .execute(username, true);

        ValidationResult passwordValidation = authUseCase
                .getValidatorUseCase()
                .getValidatePassword()
                .execute(password, true);

        if(!usernameValidation.getSuccessful()) {
            observeLoginUser().postValue(Resource.Failure(new IllegalArgumentException(usernameValidation.getErrorMessage())));
            return;
        }

        if(!passwordValidation.getSuccessful()) {
            observeLoginUser().postValue(Resource.Failure(new IllegalArgumentException(passwordValidation.getErrorMessage())));
            return;
        }

        authUseCase.login(username, password).subscribeOn(Schedulers.io())
                .subscribe(new SingleObserver<Resource<User>>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        disposable.add(d);
                        observeLoginUser().postValue(Resource.Loading(new User()));
                    }

                    @Override
                    public void onSuccess(@NonNull Resource<User> userResource) {
                        assert userResource.data != null;
                        observeLoginUser().postValue(Resource.Success(userResource.data));
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        observeLoginUser().postValue(Resource.Failure(e));
                    }
                });
    }

    public MutableLiveData<Resource<User>> observeLoginUser() {
        return loginUser;
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        if(disposable != null) {
            disposable.dispose();
        }
    }
}
