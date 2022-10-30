package com.lacorp.simple_chat_app.presentation.ui;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.lacorp.simple_chat_app.R;
import com.lacorp.simple_chat_app.data.entities.User;
import com.lacorp.simple_chat_app.databinding.FragmentRegisterBinding;
import com.lacorp.simple_chat_app.presentation.viewmodel.RegisterViewModel;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class RegisterFragment extends Fragment implements View.OnClickListener {

    private FragmentRegisterBinding fragmentRegisterBinding;
    private RegisterViewModel registerViewModel;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        fragmentRegisterBinding = FragmentRegisterBinding.inflate(inflater, container, false);
        return fragmentRegisterBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        registerViewModel = new ViewModelProvider(this).get(RegisterViewModel.class);

        initializeComponent();
        handleState();
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if(id == R.id.btnRegister) {
            String username = fragmentRegisterBinding.etUsername.getText().toString();
            String password = fragmentRegisterBinding.etPassword.getText().toString();
            String fullname = fragmentRegisterBinding.etFullname.getText().toString();
            User user = new User("", username, password, fullname);

            AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
            builder.setTitle("Register User")
                    .setPositiveButton("Confirm", (dialogInterface, i) -> registerViewModel.registerUser(user))
                    .setNegativeButton("Cancel", (dialogInterface, i) -> dialogInterface.cancel())
                    .show();
        }
        else if(id == R.id.tvLogin) {
            requireActivity()
                .getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container_view, LoginFragment.class, null)
                .commit();
        }
    }

    private void initializeComponent() {
        fragmentRegisterBinding.btnRegister.setOnClickListener(this);
        fragmentRegisterBinding.tvLogin.setOnClickListener(this);
    }

    private void handleState() {
        registerViewModel.registerUser.observe(getViewLifecycleOwner(), booleanResource -> {
            switch (booleanResource.status) {
                case SUCCESS: {
                    progressBarOff();
                    if(booleanResource.data != null) {
                        Toast.makeText(requireContext(), "Register successfully", Toast.LENGTH_SHORT).show();
                        requireActivity().getSupportFragmentManager().beginTransaction()
                                .replace(R.id.fragment_container_view, LoginFragment.class, null)
                                .commit();
                    }
                    break;
                }
                case FAILURE: {
                    progressBarOff();
                    assert booleanResource.throwable != null;
                    Toast.makeText(requireContext(), booleanResource.throwable.getMessage(), Toast.LENGTH_SHORT).show();
                    break;
                }
                case LOADING: {
                    progressBarOn();
                    break;
                }
            }
        });
    }

    private void progressBarOn() {
        fragmentRegisterBinding.progressBar.setVisibility(View.VISIBLE);
        fragmentRegisterBinding.layoutParent.setBackgroundTintList(AppCompatResources
                .getColorStateList(requireContext(), R.color.custom_placeholder));
    }

    private void progressBarOff() {
        fragmentRegisterBinding.progressBar.setVisibility(View.GONE);
        fragmentRegisterBinding.layoutParent.setBackgroundTintList(AppCompatResources
                .getColorStateList(requireContext(), R.color.custom_white));
    }
}