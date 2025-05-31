package com.association_coeur_de_france.controller.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.association_coeur_de_france.R;
import com.association_coeur_de_france.controller.MainActivity;
import com.association_coeur_de_france.network.ApiClient;

public class LoginFragment extends Fragment {

    private EditText emailInput, passwordInput;
    private Button loginButton;
    private TextView registerLink;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);

        emailInput = view.findViewById(R.id.email_input);
        passwordInput = view.findViewById(R.id.password_input);
        loginButton = view.findViewById(R.id.login_button);
        registerLink = view.findViewById(R.id.register_link);

        registerLink.setOnClickListener(v -> {
            if (getActivity() instanceof MainActivity) {
                ((MainActivity) getActivity()).loadFragment(new RegisterFragment());
            }
        });

        loginButton.setOnClickListener(v -> attemptLogin());

        return view;
    }

    private void attemptLogin() {
        String email = emailInput.getText().toString().trim();
        String password = passwordInput.getText().toString();

        if (TextUtils.isEmpty(email)) {
            emailInput.setError("Email requis");
            return;
        }
        if (TextUtils.isEmpty(password)) {
            passwordInput.setError("Mot de passe requis");
            return;
        }

        loginButton.setEnabled(false);

        ApiClient.getInstance(requireContext()).loginUser(email, password, new ApiClient.LoginCallback() {
            @Override
            public void onSuccess(int id, String firstName, String lastName, String email, String token,
                                  String birthdate, String address, String postalCode,
                                  String city, String country, String createdAt) {
                loginButton.setEnabled(true);
                saveSession(id, firstName, lastName, email, token, birthdate, address, postalCode, city, country, createdAt);
                Toast.makeText(getContext(), "Connexion réussie !", Toast.LENGTH_SHORT).show();
                ((MainActivity) requireActivity()).loadFragment(new HomeFragment());
            }

            @Override
            public void onError(String message) {
                loginButton.setEnabled(true);
                Toast.makeText(getContext(), "Erreur : " + message, Toast.LENGTH_LONG).show();
            }
        });

    }

    private void saveSession(int id, String firstName, String lastName, String email, String token,
                             String birthdate, String address, String postalCode,
                             String city, String country, String createdAt) {
        SharedPreferences prefs = requireContext().getSharedPreferences("user_session", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();

        editor.putInt("id", id);
        editor.putString("first_name", firstName);
        editor.putString("last_name", lastName);
        editor.putString("email", email);
        editor.putString("token", token);
        editor.putString("birthdate", birthdate);
        editor.putString("address", address);
        editor.putString("postal_code", postalCode);
        editor.putString("city", city);
        editor.putString("country", country);
        editor.putString("created_at", createdAt);

        editor.apply();
    }

}
