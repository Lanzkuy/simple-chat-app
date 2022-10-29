package com.lacorp.simple_chat_app.presentation.ui;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.lacorp.simple_chat_app.R;
import com.lacorp.simple_chat_app.data.entities.User;
import com.lacorp.simple_chat_app.databinding.FragmentLoginBinding;
import com.lacorp.simple_chat_app.presentation.viewmodel.LoginViewModel;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class LoginFragment extends Fragment {

    private FragmentLoginBinding fragmentLoginBinding;
    private LoginViewModel loginViewModel;

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

        initializeComponent();
        handleState();
    }

    private void initializeComponent() {
        fragmentLoginBinding.btnLogin.setOnClickListener(view -> {
            String username = fragmentLoginBinding.etUsername.getText().toString();
            String password = fragmentLoginBinding.etPassword.getText().toString();

            if(username.isEmpty() || password.isEmpty()) {
                Toast.makeText(requireContext(), "Please enter your username and password", Toast.LENGTH_SHORT).show();
            }
            else {
                loginViewModel.loginUser(username, password);
            }
        });
    }

    private void handleState() {
        loginViewModel.loggedInUser.observe(getViewLifecycleOwner(), userResource -> {
            switch (userResource.status) {
                case SUCCESS: {
                    fragmentLoginBinding.progressBar.setVisibility(View.GONE);
                    fragmentLoginBinding.parentLayout.setBackgroundTintList(AppCompatResources
                            .getColorStateList(requireContext(), R.color.custom_white));

                    User user = userResource.data;
                    if(user != null) {
                        if(user.getUser_id() == null) {
                            Toast.makeText(requireContext(), "Username or password was wrong", Toast.LENGTH_SHORT).show();
                            break;
                        }

                        requireActivity().getSupportFragmentManager().beginTransaction()
                                .replace(R.id.fragment_container_view, HomeFragment.class, null)
                                .commit();
                    }
                    break;
                }
                case FAILURE: {
                    fragmentLoginBinding.progressBar.setVisibility(View.GONE);
                    fragmentLoginBinding.parentLayout.setBackgroundTintList(AppCompatResources
                            .getColorStateList(requireContext(), R.color.custom_white));

                    Toast.makeText(requireContext(), "Something went wrong", Toast.LENGTH_SHORT).show();
                    break;
                }
                case LOADING: {
                    fragmentLoginBinding.progressBar.setVisibility(View.VISIBLE);
                    fragmentLoginBinding.parentLayout.setBackgroundTintList(AppCompatResources
                            .getColorStateList(requireContext(), R.color.custom_placeholder));
                    break;
                }
            }
        });
    }
}