package com.association_coeur_de_france.controller;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.association_coeur_de_france.R;
import com.association_coeur_de_france.model.UserModel;
import com.association_coeur_de_france.network.ApiClient;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {

    private EditText firstNameInput, lastNameInput, emailInput, emailConfirmInput, birthdateInput,
            addressInput, postalCodeInput, cityInput, countryInput;
    private Button registerButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_register); // Assure-toi que ce nom correspond à ton XML

        // Liaison des éléments XML
        firstNameInput = findViewById(R.id.editTextFirstName);
        lastNameInput = findViewById(R.id.editTextLastName);
        emailInput = findViewById(R.id.editTextEmail);
        emailConfirmInput = findViewById(R.id.editTextEmailConfirm);
        birthdateInput = findViewById(R.id.editTextBirthdate);
        addressInput = findViewById(R.id.editTextAddress);
        postalCodeInput = findViewById(R.id.editPostalCode);
        cityInput = findViewById(R.id.editTextCity);
        countryInput = findViewById(R.id.editTextCountry);
        registerButton = findViewById(R.id.register_button);

        registerButton.setOnClickListener(view -> {
            // Vérifier que les emails correspondent
            if (!emailInput.getText().toString().equals(emailConfirmInput.getText().toString())) {
                Toast.makeText(this, "Les emails ne correspondent pas", Toast.LENGTH_SHORT).show();
                return;
            }

            // Création du modèle
            UserModel user = new UserModel(
                    firstNameInput.getText().toString(),
                    lastNameInput.getText().toString(),
                    emailInput.getText().toString(),
                    emailConfirmInput.getText().toString(),
                    birthdateInput.getText().toString(),
                    addressInput.getText().toString(),
                    postalCodeInput.getText().toString(),
                    cityInput.getText().toString(),
                    countryInput.getText().toString()
            );

            // Envoi à l’API
            sendUserData(user);
        });
    }

    private void sendUserData(UserModel user) {
        String url = "http://10.0.2.2/association/api/register.php";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                response -> Toast.makeText(this, "Inscription réussie !", Toast.LENGTH_SHORT).show(),
                error -> Toast.makeText(this, "Erreur réseau : " + error.getMessage(), Toast.LENGTH_LONG).show()
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("first_name", user.getFirstName());
                params.put("last_name", user.getLastName());
                params.put("email", user.getEmail());
                params.put("email_confirmation", user.getEmailConfirmation());
                params.put("birthdate", user.getBirthdate());
                params.put("address", user.getAddress());
                params.put("postal_code", user.getPostalCode());
                params.put("city", user.getCity());
                params.put("country", user.getCountry());
                return params;
            }
        };

        ApiClient.getInstance(this).getRequestQueue().add(stringRequest);
    }
}
