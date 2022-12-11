package com.lacorp.simple_chat_app.presentation.ui;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
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
import com.lacorp.simple_chat_app.databinding.FragmentChangePasswordBinding;
import com.lacorp.simple_chat_app.databinding.FragmentFriendRequestBinding;
import com.lacorp.simple_chat_app.databinding.FragmentLoginBinding;
import com.lacorp.simple_chat_app.domain.entities.User;
import com.lacorp.simple_chat_app.presentation.adapter.FriendRequestAdapter;
import com.lacorp.simple_chat_app.presentation.viewmodel.ChangePasswordViewModel;
import com.lacorp.simple_chat_app.presentation.viewmodel.FriendRequestViewModel;
import com.lacorp.simple_chat_app.presentation.viewmodel.LoginViewModel;

import java.util.Objects;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class ChangePasswordFragment extends Fragment implements View.OnClickListener  {

    private FragmentChangePasswordBinding fragmentChangePasswordBinding;
    private ChangePasswordViewModel changePasswordViewModel;

    @Inject
    public SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        fragmentChangePasswordBinding = FragmentChangePasswordBinding.inflate(inflater, container, false);
        return fragmentChangePasswordBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        changePasswordViewModel = new ViewModelProvider(this).get(ChangePasswordViewModel.class);

        initializeComponent();
        try {
            observeAcceptFriendRequest();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if(id == R.id.btnSubmit) {
            String user_id = sharedPreferences.getString("user_id", null);
            String oldPassword = fragmentChangePasswordBinding.etOldPassword.getText().toString();
            String newPassword = fragmentChangePasswordBinding.etNewPassword.getText().toString();

            if(oldPassword.isEmpty()) {
                Toast.makeText(requireContext(), "Old password must be filled", Toast.LENGTH_SHORT).show();
                return;
            }

            if(newPassword.isEmpty()) {
                Toast.makeText(requireContext(), "New password must be filled", Toast.LENGTH_SHORT).show();
                return;
            }

            AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
            builder.setTitle("Change Password")
                    .setPositiveButton("Confirm", (dialogInterface, i) ->
                            changePasswordViewModel.changePassword(user_id, oldPassword, newPassword))
                    .setNegativeButton("Cancel", (dialogInterface, i) -> dialogInterface.cancel())
                    .show();
        }
    }

    private void initializeComponent() {
        editor = sharedPreferences.edit();
        fragmentChangePasswordBinding.btnSubmit.setOnClickListener(this);
    }

    private void observeAcceptFriendRequest() throws Exception {
        try {
            changePasswordViewModel.observeChangePassword().observe(getViewLifecycleOwner(), changePasswordResource -> {
                switch (changePasswordResource.status) {
                    case SUCCESS: {
                        progressBarOff();
                        if(changePasswordResource.data != null) {
                            Toast.makeText(requireContext(), "Change password success", Toast.LENGTH_SHORT).show();
                            editor.putString("user_id", "").apply();
                            requireActivity().getSupportFragmentManager().popBackStack();
                            requireActivity().getSupportFragmentManager().beginTransaction()
                                    .replace(R.id.fragment_container_view, LoginFragment.class, null)
                                    .commit();
                        }
                        break;
                    }
                    case FAILURE: {
                        progressBarOff();
                        assert changePasswordResource.throwable != null;
                        Toast.makeText(requireContext(), changePasswordResource.throwable.getMessage(), Toast.LENGTH_SHORT).show();
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
        fragmentChangePasswordBinding.progressBar.setVisibility(View.VISIBLE);
        fragmentChangePasswordBinding.layoutParent.setBackgroundTintList(AppCompatResources
                .getColorStateList(requireContext(), R.color.custom_placeholder));
    }

    private void progressBarOff() {
        fragmentChangePasswordBinding.progressBar.setVisibility(View.GONE);
        fragmentChangePasswordBinding.layoutParent.setBackgroundTintList(AppCompatResources
                .getColorStateList(requireContext(), R.color.custom_white));
    }
}