package com.lacorp.simple_chat_app.presentation.ui;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.core.view.MenuHost;
import androidx.core.view.MenuProvider;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.text.InputType;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.lacorp.simple_chat_app.R;
import com.lacorp.simple_chat_app.domain.entities.Friend;
import com.lacorp.simple_chat_app.domain.entities.User;
import com.lacorp.simple_chat_app.databinding.FragmentHomeBinding;
import com.lacorp.simple_chat_app.presentation.adapter.ChatRoomAdapter;
import com.lacorp.simple_chat_app.presentation.viewmodel.HomeViewModel;

import java.util.List;
import java.util.Objects;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class HomeFragment extends Fragment {

    private FragmentHomeBinding fragmentHomeBinding;
    private HomeViewModel homeViewModel;
    private ChatRoomAdapter chatRoomAdapter;

    @Inject
    public SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        fragmentHomeBinding = FragmentHomeBinding.inflate(inflater, container, false);
        return fragmentHomeBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);
        AppCompatActivity activity = (AppCompatActivity)requireActivity();
        Objects.requireNonNull(activity.getSupportActionBar()).show();
        Objects.requireNonNull(activity.getSupportActionBar()).setTitle("Chat App");

        initializeComponent();
        try {
            observeGetFriends();
            observeAddFriendStatus();
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        MenuHost menuHost = requireActivity();
        menuHost.addMenuProvider(new MenuProvider() {
            @Override
            public void onCreateMenu(@NonNull Menu menu, @NonNull MenuInflater menuInflater) {
                menuInflater.inflate(R.menu.home_menu, menu);
            }

            @Override
            public boolean onMenuItemSelected(@NonNull MenuItem menuItem) {
                if(menuItem.getItemId() == R.id.menuAddFriends) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
                    builder.setTitle("Add Friend");

                    final EditText etUsername = new EditText(requireContext());
                    etUsername.setInputType(InputType.TYPE_CLASS_TEXT);
                    builder.setView(etUsername);

                    builder.setPositiveButton("Ok", (dialogInterface, i) -> {
                        homeViewModel.addFriend(sharedPreferences.getString("user_id", null)
                                , etUsername.getText().toString());
                    }).show();
                }
                else if(menuItem.getItemId() == R.id.menuFriendRequests) {
                    requireActivity().getSupportFragmentManager().beginTransaction()
                            .replace(R.id.fragment_container_view, FriendRequestFragment.class, null)
                            .addToBackStack(null)
                            .commit();
                }
                else if(menuItem.getItemId() == R.id.menuLogout) {
                    editor.putString("user_id", "").apply();
                    requireActivity().getSupportFragmentManager().beginTransaction()
                            .replace(R.id.fragment_container_view, LoginFragment.class, null)
                            .commit();
                }
                return true;
            }
        }, getViewLifecycleOwner(), Lifecycle.State.RESUMED);
    }

    private void initializeComponent() {
        editor = sharedPreferences.edit();
        homeViewModel.getFriends(sharedPreferences.getString("user_id", null));
        fragmentHomeBinding.rvChatRoom.setLayoutManager(new LinearLayoutManager(requireContext(),
                LinearLayoutManager.VERTICAL, false));
    }

    private void observeGetFriends() throws Exception {
        try {
            homeViewModel.observeFriends().observe(getViewLifecycleOwner(), friendsResource -> {
                switch (friendsResource.status) {
                    case SUCCESS: {
                        progressBarOff();
                        if(friendsResource.data != null) {
                            List<Friend> friendList = friendsResource.data;
                            chatRoomAdapter = new ChatRoomAdapter(friendList);
                            chatRoomAdapter.setOnClickListener((view, position) -> {
                                Bundle bundle = new Bundle();
                                bundle.putString("user_id", sharedPreferences.getString("user_id", null));
                                bundle.putString("friend_id", friendList.get(position).getUser_id());
                                bundle.putString("friend_name", friendList.get(position).getFullname());

                                ChatFragment chatFragment = new ChatFragment();
                                chatFragment.setArguments(bundle);

                                requireActivity().getSupportFragmentManager().beginTransaction()
                                        .replace(R.id.fragment_container_view, chatFragment, null)
                                        .addToBackStack(null)
                                        .commit();
                            });
                            fragmentHomeBinding.rvChatRoom.setAdapter(chatRoomAdapter);
                            fragmentHomeBinding.rvChatRoom.scrollToPosition(chatRoomAdapter.getItemCount() - 1);
                        }
                        break;
                    }
                    case FAILURE: {
                        progressBarOff();
                        assert friendsResource.throwable != null;
                        Toast.makeText(requireContext(), friendsResource.throwable.getMessage(), Toast.LENGTH_SHORT).show();
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

    private void observeAddFriendStatus() throws Exception {
        try {
            homeViewModel.observeAddFriendsStatus().observe(getViewLifecycleOwner(), addFriendStatusResource -> {
                switch (addFriendStatusResource.status) {
                    case SUCCESS: {
                        progressBarOff();
                        if(addFriendStatusResource.data != null) {
                            Toast.makeText(requireContext(), "Friend request sent", Toast.LENGTH_SHORT).show();
                        }
                        break;
                    }
                    case FAILURE: {
                        progressBarOff();
                        assert addFriendStatusResource.throwable != null;
                        Toast.makeText(requireContext(), addFriendStatusResource.throwable.getMessage(), Toast.LENGTH_SHORT).show();
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
        fragmentHomeBinding.progressBar.setVisibility(View.VISIBLE);
        fragmentHomeBinding.layoutParent.setBackgroundTintList(AppCompatResources
                .getColorStateList(requireContext(), R.color.custom_placeholder));
    }

    private void progressBarOff() {
        fragmentHomeBinding.progressBar.setVisibility(View.GONE);
        fragmentHomeBinding.layoutParent.setBackgroundTintList(AppCompatResources
                .getColorStateList(requireContext(), R.color.custom_white));
    }
}