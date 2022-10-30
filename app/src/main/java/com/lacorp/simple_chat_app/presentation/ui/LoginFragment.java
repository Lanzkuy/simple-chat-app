package com.lacorp.simple_chat_app.presentation.ui;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.lacorp.simple_chat_app.R;
import com.lacorp.simple_chat_app.databinding.FragmentLoginBinding;
import com.lacorp.simple_chat_app.presentation.viewmodel.LoginViewModel;

import java.util.Objects;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class LoginFragment extends Fragment implements View.OnClickListener {

    private FragmentLoginBinding fragmentLoginBinding;
    private LoginViewModel loginViewModel;

    @Inject
    public SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        fragmentLoginBinding = FragmentLoginBinding.inflate(inflater, container, false);
        return fragmentLoginBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        loginViewModel = new ViewModelProvider(this).get(LoginViewModel.class);

        Objects.requireNonNull(((AppCompatActivity) requireActivity()).getSupportActionBar()).hide();
        checkSession();
        initializeComponent();
        handleState();
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if(id == R.id.btnLogin) {
            String username = fragmentLoginBinding.etUsername.getText().toString();
            String password = fragmentLoginBinding.etPassword.getText().toString();
            loginViewModel.loginUser(username, password);
        }

        if(id == R.id.tvRegister) {
            requireActivity()
                .getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container_view, RegisterFragment.class, null)
                .commit();
        }
    }

    private void checkSession() {
        String user_id = sharedPreferences.getString("user_id", null);
        if(user_id != null) {
            if(!user_id.isEmpty()) {
                requireActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container_view, HomeFragment.class, null)
                        .commit();
            }
        }
    }

    private void initializeComponent() {
        editor = sharedPreferences.edit();
        fragmentLoginBinding.btnLogin.setOnClickListener(this);
        fragmentLoginBinding.tvRegister.setOnClickListener(this);
    }

    private void handleState() {
        loginViewModel.loggedInUser.observe(getViewLifecycleOwner(), userResource -> {
            switch (userResource.status) {
                case SUCCESS: {
                    progressBarOff();
                    if(userResource.data != null) {
                        editor.putString("user_id", userResource.data.getUser_id());
                        editor.apply();

                        requireActivity().getSupportFragmentManager().beginTransaction()
                                .replace(R.id.fragment_container_view, HomeFragment.class, null)
                                .commit();
                    }
                    break;
                }
                case FAILURE: {
                    progressBarOff();
                    assert userResource.throwable != null;
                    Toast.makeText(requireContext(), userResource.throwable.getMessage(), Toast.LENGTH_SHORT).show();
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
        fragmentLoginBinding.progressBar.setVisibility(View.VISIBLE);
        fragmentLoginBinding.layoutParent.setBackgroundTintList(AppCompatResources
                .getColorStateList(requireContext(), R.color.custom_placeholder));
    }

    private void progressBarOff() {
        fragmentLoginBinding.progressBar.setVisibility(View.GONE);
        fragmentLoginBinding.layoutParent.setBackgroundTintList(AppCompatResources
                .getColorStateList(requireContext(), R.color.custom_white));
    }
}