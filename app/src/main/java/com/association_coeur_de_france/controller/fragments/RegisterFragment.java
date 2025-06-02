package com.association_coeur_de_france.controller.fragments;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
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
            UserModel user = validateAndCreateUser();
            if (user == null) return;

            registerButton.setEnabled(false); // Désactive le bouton pendant la requête

            sendUserData(user);
        });

        return view;
    }

    /**
     * Valide les champs, affiche les erreurs via Toast si besoin, et crée l'objet UserModel.
     * @return UserModel si valide, sinon null
     */
    private UserModel validateAndCreateUser() {
        String firstName = firstNameInput.getText().toString().trim();
        String lastName = lastNameInput.getText().toString().trim();
        String email = emailInput.getText().toString().trim();
        String password = passwordInput.getText().toString();
        String passwordConfirm = passwordInputConfirmation.getText().toString();
        String birthdate = birthdateInput.getText().toString().trim();
        String address = addressInput.getText().toString().trim();
        String postalCode = postalCodeInput.getText().toString().trim();
        String city = cityInput.getText().toString().trim();
        String country = countryInput.getText().toString().trim();

        if (TextUtils.isEmpty(firstName) || TextUtils.isEmpty(lastName) || TextUtils.isEmpty(email) ||
                TextUtils.isEmpty(password) || TextUtils.isEmpty(passwordConfirm) || TextUtils.isEmpty(birthdate)) {
            Toast.makeText(getContext(), "Veuillez remplir tous les champs obligatoires", Toast.LENGTH_SHORT).show();
            return null;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(getContext(), "Veuillez saisir un email valide", Toast.LENGTH_SHORT).show();
            return null;
        }

        if (!password.equals(passwordConfirm)) {
            Toast.makeText(getContext(), "Les mots de passe ne correspondent pas", Toast.LENGTH_SHORT).show();
            return null;
        }

        if (password.length() < 6) {
            Toast.makeText(getContext(), "Le mot de passe doit contenir au moins 6 caractères", Toast.LENGTH_SHORT).show();
            return null;
        }

        return new UserModel(firstName, lastName, email, password, passwordConfirm,
                birthdate, address, postalCode, city, country);
    }

    private void sendUserData(UserModel user) {
        ApiClient.getInstance(requireContext()).registerUser(user,
                response -> {
                    Toast.makeText(getContext(), "Inscription réussie !", Toast.LENGTH_SHORT).show();
                    registerButton.setEnabled(true);
                    // Tu peux aussi naviguer vers login ou autre ici
                },
                error -> {
                    if (error.getCause() != null) {
                        error.getCause().printStackTrace();
                    }
                    Toast.makeText(getContext(), "Erreur réseau : " + error.getMessage(), Toast.LENGTH_LONG).show();
                    registerButton.setEnabled(true);
                }
        );
    }
}
