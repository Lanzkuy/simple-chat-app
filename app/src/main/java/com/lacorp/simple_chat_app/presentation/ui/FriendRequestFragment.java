package com.lacorp.simple_chat_app.presentation.ui;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.core.view.MenuHost;
import androidx.core.view.MenuProvider;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.lacorp.simple_chat_app.R;
import com.lacorp.simple_chat_app.databinding.FragmentFriendRequestBinding;
import com.lacorp.simple_chat_app.domain.entities.FriendRequest;
import com.lacorp.simple_chat_app.presentation.adapter.FriendRequestAdapter;
import com.lacorp.simple_chat_app.presentation.viewmodel.FriendRequestViewModel;

import java.util.List;
import java.util.Objects;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class FriendRequestFragment extends Fragment {

    private FragmentFriendRequestBinding fragmentFriendRequestBinding;
    private FriendRequestViewModel friendRequestViewModel;
    private FriendRequestAdapter friendRequestAdapter;

    @Inject
    public SharedPreferences sharedPreferences;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        fragmentFriendRequestBinding = FragmentFriendRequestBinding.inflate(inflater, container, false);
        return fragmentFriendRequestBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        AppCompatActivity activity = (AppCompatActivity)requireActivity();
        Objects.requireNonNull(activity.getSupportActionBar()).setTitle("Friend Request");
        friendRequestViewModel = new ViewModelProvider(this).get(FriendRequestViewModel.class);

        initializeComponent();
        observeGetFriendRequests();
        observeAcceptFriendRequest();
        observeDeleteFriendRequest();
    }

    private void observeGetFriendRequests() {
        try {
            friendRequestViewModel.observeFriendRequestsState().observe(getViewLifecycleOwner(), friendRequestsResource -> {
                switch (friendRequestsResource.status) {
                    case SUCCESS: {
                        progressBarOff();
                        if(friendRequestsResource.data != null) {
                            List<FriendRequest> friendRequestList = friendRequestsResource.data;
                            friendRequestAdapter = new FriendRequestAdapter(friendRequestList);
                            friendRequestAdapter.setOnClickListener((view, friendRequest) -> friendRequestViewModel
                                    .acceptFriendRequest(sharedPreferences.getString("user_id", null), friendRequest));
                            fragmentFriendRequestBinding.rvFriendRequest.setAdapter(friendRequestAdapter);
                        }
                        break;
                    }
                    case FAILURE: {
                        progressBarOff();
                        assert friendRequestsResource.throwable != null;
                        Toast.makeText(requireContext(), friendRequestsResource.throwable.getMessage(), Toast.LENGTH_SHORT).show();
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
            Toast.makeText(requireContext(), "Something went wrong", Toast.LENGTH_SHORT).show();
        }
    }

    private void observeAcceptFriendRequest() {
        try {
            friendRequestViewModel.observeAcceptRequestState().observe(getViewLifecycleOwner(), acceptFriendRequestsResource -> {
                switch (acceptFriendRequestsResource.status) {
                    case SUCCESS: {
                        progressBarOff();
                        if(acceptFriendRequestsResource.data != null) {
                            Toast.makeText(requireContext(), "Friend request accepted", Toast.LENGTH_SHORT).show();

                            friendRequestViewModel.deleteFriendRequest(sharedPreferences.getString("user_id", null),
                                    acceptFriendRequestsResource.data.getFriend_request_id());
                        }
                        break;
                    }
                    case FAILURE: {
                        progressBarOff();
                        assert acceptFriendRequestsResource.throwable != null;
                        Toast.makeText(requireContext(), acceptFriendRequestsResource.throwable.getMessage(), Toast.LENGTH_SHORT).show();
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
            Toast.makeText(requireContext(), "Something went wrong", Toast.LENGTH_SHORT).show();
        }
    }

    private void observeDeleteFriendRequest() {
        try {
            friendRequestViewModel.observeDeleteRequestState().observe(getViewLifecycleOwner(), deleteFriendRequestsResource -> {
                switch (deleteFriendRequestsResource.status) {
                    case SUCCESS: {
                        progressBarOff();

                        requireActivity().getSupportFragmentManager().beginTransaction()
                                .replace(R.id.fragment_container_view, FriendRequestFragment.class, null)
                                .commit();
                        break;
                    }
                    case FAILURE: {
                        progressBarOff();
                        assert deleteFriendRequestsResource.throwable != null;
                        Toast.makeText(requireContext(), deleteFriendRequestsResource.throwable.getMessage(), Toast.LENGTH_SHORT).show();
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
            Toast.makeText(requireContext(), "Something went wrong", Toast.LENGTH_SHORT).show();
        }
    }

    private void initializeComponent() {
        friendRequestViewModel.getFriendRequests(sharedPreferences.getString("user_id", null));
        fragmentFriendRequestBinding.rvFriendRequest.setLayoutManager(new LinearLayoutManager(requireContext(),
                LinearLayoutManager.VERTICAL, false));
    }

    private void progressBarOn() {
        fragmentFriendRequestBinding.progressBar.setVisibility(View.VISIBLE);
        fragmentFriendRequestBinding.layoutParent.setBackgroundTintList(AppCompatResources
                .getColorStateList(requireContext(), R.color.custom_placeholder));
    }

    private void progressBarOff() {
        fragmentFriendRequestBinding.progressBar.setVisibility(View.GONE);
        fragmentFriendRequestBinding.layoutParent.setBackgroundTintList(AppCompatResources
                .getColorStateList(requireContext(), R.color.custom_white));
    }
}