package com.association_coeur_de_france.controller.fragments;

import android.os.Bundle;
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
import com.association_coeur_de_france.model.UserModel;
import com.association_coeur_de_france.network.ApiClient;

public class RegisterFragment extends Fragment {

    private EditText firstNameInput, lastNameInput, emailInput, passwordInput, passwordInputConfirmation, birthdateInput,
            addressInput, postalCodeInput, cityInput, countryInput;
    private Button registerButton;
    private TextView loginLink;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_register, container, false);

        // Liaison des vues
        firstNameInput = view.findViewById(R.id.editTextFirstName);
        lastNameInput = view.findViewById(R.id.editTextLastName);
        emailInput = view.findViewById(R.id.editTextEmail);
        passwordInput = view.findViewById(R.id.editTextPassword);
        passwordInputConfirmation = view.findViewById(R.id.editTextPasswordConfirm);
        birthdateInput = view.findViewById(R.id.editTextBirthdate);
        addressInput = view.findViewById(R.id.editTextAddress);
        postalCodeInput = view.findViewById(R.id.editPostalCode);
        cityInput = view.findViewById(R.id.editTextCity);
        countryInput = view.findViewById(R.id.editTextCountry);
        registerButton = view.findViewById(R.id.register_button);
        loginLink = view.findViewById(R.id.login_link);

        // Clic sur "Déjà un compte ? Se connecter"
        loginLink.setOnClickListener(v -> {
            if (getActivity() instanceof MainActivity) {
                ((MainActivity) getActivity()).loadFragment(new LoginFragment());
            }
        });

        registerButton.setOnClickListener(v -> {
            if (!passwordInput.getText().toString().equals(passwordInputConfirmation.getText().toString())) {
                Toast.makeText(getContext(), "Les mots de passe ne correspondent pas", Toast.LENGTH_SHORT).show();
                return;
            }

            UserModel user = new UserModel(
                    firstNameInput.getText().toString(),
                    lastNameInput.getText().toString(),
                    emailInput.getText().toString(),
                    passwordInput.getText().toString(),
                    passwordInputConfirmation.getText().toString(),
                    birthdateInput.getText().toString(),
                    addressInput.getText().toString(),
                    postalCodeInput.getText().toString(),
                    cityInput.getText().toString(),
                    countryInput.getText().toString()
            );

            sendUserData(user);
        });

        return view;
    }

    private void sendUserData(UserModel user) {
        ApiClient.getInstance(requireContext()).registerUser(user,
                response -> Toast.makeText(getActivity(), "Inscription réussie !", Toast.LENGTH_SHORT).show(),
                error -> {
                    if (error.getCause() != null) {
                        error.getCause().printStackTrace();
                    }
                    Toast.makeText(getActivity(), "Erreur réseau : " + error.getMessage(), Toast.LENGTH_LONG).show();
                }
        );
    }

}
