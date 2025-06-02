package com.association_coeur_de_france.controller.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.association_coeur_de_france.R;
import com.association_coeur_de_france.SessionManager;
import com.association_coeur_de_france.controller.MainActivity;
import com.association_coeur_de_france.network.ApiClient;

import org.json.JSONException;
import org.json.JSONObject;

public class InfosFragment extends Fragment {

    private EditText etFirstName, etLastName, etBirthdate, etEmail, etAddress, etPostalCode, etCity, etCountry;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_infos, container, false);

        // Récupération des vues
        etFirstName = view.findViewById(R.id.etFirstName);
        etLastName = view.findViewById(R.id.etLastName);
        etBirthdate = view.findViewById(R.id.etBirthdate);
        etEmail = view.findViewById(R.id.etEmail);
        etAddress = view.findViewById(R.id.etAddress);
        etPostalCode = view.findViewById(R.id.etPostalCode);
        etCity = view.findViewById(R.id.etCity);
        etCountry = view.findViewById(R.id.etCountry);
        Button btnRegister = view.findViewById(R.id.btnRegister);

        // Charger les données utilisateur depuis l’API
        loadUserData();

        btnRegister.setOnClickListener(v -> {
            if (validateInputs()) {
                if (getActivity() instanceof MainActivity) {
                    ((MainActivity) getActivity()).loadFragment(new RecapitulatifFragment());
                }
            } else {
                Toast.makeText(getContext(), "Veuillez remplir tous les champs", Toast.LENGTH_SHORT).show();
            }
        });


        return view;
    }



    private boolean validateInputs() {
        return !etFirstName.getText().toString().trim().isEmpty() &&
                !etLastName.getText().toString().trim().isEmpty() &&
                !etBirthdate.getText().toString().trim().isEmpty() &&
                !etEmail.getText().toString().trim().isEmpty() &&
                !etAddress.getText().toString().trim().isEmpty() &&
                !etPostalCode.getText().toString().trim().isEmpty() &&
                !etCity.getText().toString().trim().isEmpty() &&
                !etCountry.getText().toString().trim().isEmpty();
    }


    private void loadUserData() {
        SessionManager sessionManager = new SessionManager(requireContext());
        int userId = sessionManager.getId();

        if (userId == -1) {
            Toast.makeText(getContext(), "Utilisateur non connecté", Toast.LENGTH_SHORT).show();
            return;
        }

        ApiClient.getInstance(getContext()).getUserById(userId, response -> {
                try {
                    JSONObject json = new JSONObject(response);

                    if (json.has("error")) {
                        Toast.makeText(getContext(), json.getString("error"), Toast.LENGTH_SHORT).show();
                        return;
                    }

                    // Remplir les EditText avec les infos
                    etFirstName.setText(json.optString("first_name", ""));
                    etLastName.setText(json.optString("last_name", ""));
                    etBirthdate.setText(json.optString("birthdate", ""));
                    etEmail.setText(json.optString("email", ""));
                    etAddress.setText(json.optString("address", ""));
                    etPostalCode.setText(json.optString("postal_code", ""));
                    etCity.setText(json.optString("city", ""));
                    etCountry.setText(json.optString("country", ""));

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getContext(), "Erreur lors de la lecture des données utilisateur", Toast.LENGTH_SHORT).show();
                }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                Toast.makeText(getContext(), "Erreur réseau : " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}

