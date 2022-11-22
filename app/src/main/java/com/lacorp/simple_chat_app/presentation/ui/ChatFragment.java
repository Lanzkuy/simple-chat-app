package com.lacorp.simple_chat_app.presentation.ui;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.lacorp.simple_chat_app.R;
import com.lacorp.simple_chat_app.domain.entities.Message;
import com.lacorp.simple_chat_app.databinding.FragmentChatBinding;
import com.lacorp.simple_chat_app.presentation.adapter.ChatMessageAdapter;
import com.lacorp.simple_chat_app.presentation.viewmodel.ChatViewModel;

import java.util.Date;
import java.util.List;
import java.util.Objects;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class ChatFragment extends Fragment implements View.OnClickListener{

    private FragmentChatBinding fragmentChatBinding;
    private ChatViewModel chatViewModel;
    private ChatMessageAdapter chatMessageAdapter;
    private String user_id, friend_id, friend_name;

    @Inject
    public SharedPreferences sharedPreferences;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        fragmentChatBinding = FragmentChatBinding.inflate(inflater, container, false);

        Bundle bundle = this.getArguments();
        if(bundle != null) {
            user_id = bundle.getString("user_id");
            friend_id = bundle.getString("friend_id");
            friend_name = bundle.getString("friend_name");
        }

        return fragmentChatBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        chatViewModel = new ViewModelProvider(this).get(ChatViewModel.class);
        Objects.requireNonNull(((AppCompatActivity) requireActivity())
                .getSupportActionBar()).setTitle(friend_name);

        initializeComponent();
        try {
            observeMessages();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if(id == R.id.btnSend) {
            String messageText = fragmentChatBinding.etMessage.getText().toString();
            Message message = new Message(user_id, messageText, new Date());
            chatViewModel.sendMessage(message, friend_id);

            chatViewModel.observeSendMessage().observe(getViewLifecycleOwner(), messageResource -> {
                switch (messageResource.status) {
                    case SUCCESS: {
                        fragmentChatBinding.etMessage.setText("");
                        if(messageResource.data != null) {
                            fragmentChatBinding.rvMessages.scrollToPosition(chatMessageAdapter.getItemCount() - 1);
                        }
                        break;
                    }
                    case FAILURE: {
                        assert messageResource.throwable != null;
                        Toast.makeText(requireContext(), messageResource.throwable.getMessage(), Toast.LENGTH_SHORT).show();
                        break;
                    }
                    case LOADING: {
                        break;
                    }
                }
            });
        }
    }

    private void initializeComponent() {
        chatViewModel.getMessages(user_id, friend_id);
        fragmentChatBinding.btnSend.setOnClickListener(this);
        fragmentChatBinding.rvMessages.setLayoutManager(new LinearLayoutManager(requireContext(),
                LinearLayoutManager.VERTICAL, false));
    }

    private void observeMessages() throws Exception {
        try {
            chatViewModel.observeGetMessages().observe(getViewLifecycleOwner(), messageResource -> {
                switch (messageResource.status) {
                    case SUCCESS: {
                        if(messageResource.data != null) {
                            progressBarOff();
                            List<Message> messageList = messageResource.data;
                            chatMessageAdapter = new ChatMessageAdapter(messageList, sharedPreferences.getString("user_id", null));
                            fragmentChatBinding.rvMessages.setAdapter(chatMessageAdapter);
                            fragmentChatBinding.rvMessages.scrollToPosition(chatMessageAdapter.getItemCount() - 1);
                        }
                        break;
                    }
                    case FAILURE: {
                        progressBarOff();
                        assert messageResource.throwable != null;
                        Toast.makeText(requireContext(), messageResource.throwable.getMessage(), Toast.LENGTH_SHORT).show();
                        break;
                    }
                    case LOADING: {
                        progressBarOn();
                        break;
                    }
                }
            });
        }
        catch (Exception ex) {
            throw new Exception();
        }
    }

    private void progressBarOn() {
        fragmentChatBinding.progressBar.setVisibility(View.VISIBLE);
        fragmentChatBinding.layoutParent.setBackgroundTintList(AppCompatResources
                .getColorStateList(requireContext(), R.color.custom_placeholder));
    }

    private void progressBarOff() {
        fragmentChatBinding.progressBar.setVisibility(View.GONE);
        fragmentChatBinding.layoutParent.setBackgroundTintList(AppCompatResources
                .getColorStateList(requireContext(), R.color.custom_white));
    }
}