package com.lacorp.simple_chat_app.presentation.viewmodel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.lacorp.simple_chat_app.domain.entities.User;
import com.lacorp.simple_chat_app.domain.usecase.AuthUseCase;
import com.lacorp.simple_chat_app.domain.usecase.validator.ValidationResult;
import com.lacorp.simple_chat_app.utils.Resource;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.CompletableObserver;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

@HiltViewModel
public class RegisterViewModel extends ViewModel {

    private final MutableLiveData<Resource<Boolean>> registerState = new MutableLiveData<>();
    private final AuthUseCase authUseCase;
    private final CompositeDisposable disposable = new CompositeDisposable();

    @Inject
    public RegisterViewModel(AuthUseCase registerUseCase) {
        this.authUseCase = registerUseCase;
    }

    public void registerUser(User user) {
        ValidationResult usernameValidation= authUseCase
                .getValidatorUseCase()
                .getValidateUsername()
                .execute(user.getUsername(), false);

        ValidationResult passwordValidation = authUseCase
                .getValidatorUseCase()
                .getValidatePassword()
                .execute(user.getPassword(), false);

        ValidationResult fullnameValidation = authUseCase
                .getValidatorUseCase()
                .getValidateFullname()
                .execute(user.getFullname());

        if(!usernameValidation.getSuccessful()) {
            observeRegisterState().postValue(Resource.Failure(new IllegalArgumentException(usernameValidation.getErrorMessage())));
            return;
        }

        if(!passwordValidation.getSuccessful()) {
            observeRegisterState().postValue(Resource.Failure(new IllegalArgumentException(passwordValidation.getErrorMessage())));
            return;
        }

        if(!fullnameValidation.getSuccessful()) {
            observeRegisterState().postValue(Resource.Failure(new IllegalArgumentException(fullnameValidation.getErrorMessage())));
            return;
        }

        authUseCase.register(user)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new CompletableObserver() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        disposable.add(d);
                        observeRegisterState().postValue(Resource.Loading(true));
                    }

                    @Override
                    public void onComplete() {
                        observeRegisterState().postValue(Resource.Success(true));
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        observeRegisterState().postValue(Resource.Failure(e));
                    }
                });
    }

    public MutableLiveData<Resource<Boolean>> observeRegisterState() {
        return registerState;
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        if(disposable != null) {
            disposable.dispose();
        }
    }
}
