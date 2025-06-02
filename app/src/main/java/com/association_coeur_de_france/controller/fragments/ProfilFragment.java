package com.association_coeur_de_france.controller.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.association_coeur_de_france.R;
import com.association_coeur_de_france.SessionManager;
import com.association_coeur_de_france.controller.MainActivity;
import com.association_coeur_de_france.model.UserModel;
import com.association_coeur_de_france.network.ApiClient;

import org.json.JSONException;
import org.json.JSONObject;

public class ProfilFragment extends Fragment {

    private TextView fullNameTextView, emailTextView, birthDateTextView,
            addressTextView, postalTextView, cityTextView, countryTextView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profil, container, false);

        fullNameTextView = view.findViewById(R.id.fullNameText);
        emailTextView = view.findViewById(R.id.emailText);
        birthDateTextView = view.findViewById(R.id.birthdateText);
        addressTextView = view.findViewById(R.id.addressText);
        postalTextView = view.findViewById(R.id.postalCodeText);
        cityTextView = view.findViewById(R.id.cityText);
        countryTextView = view.findViewById(R.id.countryText);

        Button logoutButton = view.findViewById(R.id.logoutButton);

        loadUserData();

        logoutButton.setOnClickListener(v -> {
            SessionManager sessionManager = new SessionManager(requireContext());
            sessionManager.clearSession();

            Toast.makeText(getContext(), "Déconnexion réussie", Toast.LENGTH_SHORT).show();
            ((MainActivity) requireActivity()).loadFragment(new LoginFragment());
        });

        return view;
    }

    private void loadUserData() {
        SessionManager sessionManager = new SessionManager(requireContext());
        int userId = sessionManager.getId();  // Il faut ajouter cette méthode dans SessionManager

        if (userId == -1) {
            Toast.makeText(getContext(), "Utilisateur non connecté", Toast.LENGTH_SHORT).show();
            return;
        }

        ApiClient.getInstance(getContext()).getUserById(userId, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject json = new JSONObject(response);

                    if (json.has("error")) {
                        Toast.makeText(getContext(), json.getString("error"), Toast.LENGTH_SHORT).show();
                        return;
                    }

                    UserModel user = new UserModel();
                    user.setFirstName(json.optString("first_name", ""));
                    user.setLastName(json.optString("last_name", ""));
                    user.setEmail(json.optString("email", ""));
                    user.setBirthdate(json.optString("birthdate", ""));
                    user.setAddress(json.optString("address", ""));
                    user.setPostalCode(json.optString("postal_code", ""));
                    user.setCity(json.optString("city", ""));
                    user.setCountry(json.optString("country", ""));

                    fullNameTextView.setText("Nom complet : " + user.getLastName() + " " + user.getFirstName());
                    emailTextView.setText("Email : " + user.getEmail());
                    birthDateTextView.setText("Date de naissance : " + user.getBirthdate());
                    addressTextView.setText("Adresse : " + user.getAddress());
                    postalTextView.setText("Code postal : " + user.getPostalCode());
                    cityTextView.setText("Ville : " + user.getCity());
                    countryTextView.setText("Pays : " + user.getCountry());

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getContext(), "Erreur lors de la lecture des données utilisateur", Toast.LENGTH_SHORT).show();
                }
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
