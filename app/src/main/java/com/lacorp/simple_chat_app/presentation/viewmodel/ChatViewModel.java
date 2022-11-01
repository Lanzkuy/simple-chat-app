package com.lacorp.simple_chat_app.presentation.viewmodel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.lacorp.simple_chat_app.data.entities.Message;
import com.lacorp.simple_chat_app.domain.usecase.ChatUseCase;
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
public class ChatViewModel extends ViewModel {

    public MutableLiveData<Resource<List<Message>>> getMessages = new MutableLiveData<>();
    public MutableLiveData<Resource<Boolean>> sendMessage = new MutableLiveData<>();
    private final ChatUseCase chatUseCase;
    private final CompositeDisposable disposable = new CompositeDisposable();

    @Inject
    public ChatViewModel(ChatUseCase chatUseCase) {
        this.chatUseCase = chatUseCase;
    }

    public void getMessages(String user_id, String friend_id) {
        chatUseCase.getMessages(user_id, friend_id).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .toObservable().
                subscribe(new Observer<Resource<List<Message>>>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        disposable.add(d);
                        getMessages.postValue(Resource.Loading(new ArrayList<>()));
                    }

                    @Override
                    public void onNext(@NonNull Resource<List<Message>> listResource) {
                        getMessages.postValue(listResource);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        getMessages.postValue(Resource.Failure(e));
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    public void sendMessage(Message message, String friend_id) {
        if(message.getMessage().isEmpty()) {
            return;
        }

        chatUseCase.sendMessage(message, friend_id).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new CompletableObserver() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        disposable.add(d);
                        sendMessage.postValue(Resource.Loading(true));
                    }

                    @Override
                    public void onComplete() {
                        sendMessage.postValue(Resource.Success(true));
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        sendMessage.postValue(Resource.Failure(e));
                    }
                });
    }
}
