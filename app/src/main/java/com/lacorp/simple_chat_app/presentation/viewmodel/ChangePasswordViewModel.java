package com.lacorp.simple_chat_app.presentation.viewmodel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.lacorp.simple_chat_app.domain.usecase.UserUseCase;
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
public class ChangePasswordViewModel extends ViewModel {

    private final MutableLiveData<Resource<Boolean>> changePasswordState = new MutableLiveData<>();
    private final UserUseCase userUseCase;
    private final CompositeDisposable disposable = new CompositeDisposable();

    @Inject
    public ChangePasswordViewModel(UserUseCase userUseCase) { this.userUseCase = userUseCase; }

    public void changePassword(String user_id, String oldPassword, String newPassword) {
        ValidationResult oldPasswordValidation= userUseCase
                .getValidatorUseCase()
                .getValidateUsername()
                .execute(oldPassword, false);

        ValidationResult newPasswordValidation = userUseCase
                .getValidatorUseCase()
                .getValidatePassword()
                .execute(newPassword, false);

        if(!oldPasswordValidation.getSuccessful()) {
            observeChangePasswordState()
                    .postValue(Resource.Failure(
                            new IllegalArgumentException(oldPasswordValidation.getErrorMessage())));
            return;
        }

        if(!newPasswordValidation.getSuccessful()) {
            observeChangePasswordState()
                    .postValue(Resource.Failure(
                            new IllegalArgumentException(newPasswordValidation.getErrorMessage())));
            return;
        }

        userUseCase.changePassword(user_id, oldPassword, newPassword)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new CompletableObserver() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        disposable.add(d);
                        observeChangePasswordState().postValue(Resource.Loading(true));
                    }

                    @Override
                    public void onComplete() {
                        observeChangePasswordState().postValue(Resource.Success(true));
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        observeChangePasswordState().postValue(Resource.Failure(e));
                    }
                });
    }

    public MutableLiveData<Resource<Boolean>> observeChangePasswordState() {
        return changePasswordState;
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        if(disposable != null) {
            disposable.dispose();
        }
    }
}
