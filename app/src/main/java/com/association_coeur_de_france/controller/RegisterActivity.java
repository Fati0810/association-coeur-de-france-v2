package com.association_coeur_de_france.controller;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.association_coeur_de_france.R;
import com.association_coeur_de_france.model.UserModel;
import com.association_coeur_de_france.network.ApiClient;
import com.android.volley.Response;

public class RegisterActivity extends AppCompatActivity {

    private EditText firstNameInput, lastNameInput, emailInput, passwordInput, passwordInputConfirm, birthdateInput,
            addressInput, postalCodeInput, cityInput, countryInput;
    private Button registerButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_register); // Vérifie que c’est bien ce layout

        // Liaison des vues
        firstNameInput = findViewById(R.id.editTextFirstName);
        lastNameInput = findViewById(R.id.editTextLastName);
        emailInput = findViewById(R.id.editTextEmail);
        birthdateInput = findViewById(R.id.editTextBirthdate);
        addressInput = findViewById(R.id.editTextAddress);
        passwordInput = findViewById(R.id.editTextPassword);
        passwordInputConfirm = findViewById(R.id.editTextPasswordConfirm);
        postalCodeInput = findViewById(R.id.editPostalCode);
        cityInput = findViewById(R.id.editTextCity);
        countryInput = findViewById(R.id.editTextCountry);
        registerButton = findViewById(R.id.register_button);

        registerButton.setOnClickListener(view -> {
            String firstName = firstNameInput.getText().toString().trim();
            String lastName = lastNameInput.getText().toString().trim();
            String email = emailInput.getText().toString().trim();
            String password = passwordInput.getText().toString();
            String passwordConfirm = passwordInputConfirm.getText().toString();
            String birthdate = birthdateInput.getText().toString().trim();
            String address = addressInput.getText().toString().trim();
            String postalCode = postalCodeInput.getText().toString().trim();
            String city = cityInput.getText().toString().trim();
            String country = countryInput.getText().toString().trim();

            if (firstName.isEmpty() || lastName.isEmpty() || email.isEmpty() || password.isEmpty() || birthdate.isEmpty()) {
                Toast.makeText(this, "Veuillez remplir tous les champs obligatoires", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                Toast.makeText(this, "Veuillez saisir un email valide", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!password.equals(passwordConfirm)) {
                Toast.makeText(this, "Les mots de passe ne correspondent pas", Toast.LENGTH_SHORT).show();
                return;
            }

            if (password.length() < 6) {
                Toast.makeText(this, "Le mot de passe doit contenir au moins 6 caractères", Toast.LENGTH_SHORT).show();
                return;
            }

            // Création du modèle utilisateur
            UserModel user = new UserModel(
                    firstName, lastName, email, password, passwordConfirm,
                    birthdate, address, postalCode, city, country
            );

            sendUserData(user);
        });

    }

    private void sendUserData(UserModel user) {
        ApiClient.getInstance(this).registerUser(user,
                response -> Toast.makeText(this, "Inscription réussie !", Toast.LENGTH_SHORT).show(),
                error -> Toast.makeText(this, "Erreur réseau : " + error.getMessage(), Toast.LENGTH_LONG).show()
        );
    }
}
