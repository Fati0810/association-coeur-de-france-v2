package com.association_coeur_de_france.controller.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.association_coeur_de_france.R;
import com.association_coeur_de_france.model.DonModel;
import com.association_coeur_de_france.network.ApiClient;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Locale;

public class RecapitulatifFragment extends Fragment {
    private EditText donInput, contributionInput, totalInput;
    private CheckBox checkboxUnderstood, checkboxAccepted;
    private Button payButton;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_recapitulatif, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        donInput = view.findViewById(R.id.donInput);
        contributionInput = view.findViewById(R.id.contributionInput);
        totalInput = view.findViewById(R.id.totalInput);
        checkboxUnderstood = view.findViewById(R.id.checkboxUnderstood);
        checkboxAccepted = view.findViewById(R.id.checkboxAccepted);
        payButton = view.findViewById(R.id.payButton);

        // Récupérer le montant depuis SharedPreferences (en supposant stocké en int centimes ou arrondi)
        SharedPreferences prefs = requireContext().getSharedPreferences("don_prefs", Context.MODE_PRIVATE);
        int amount = prefs.getInt("montant_don", 0);
        donInput.setText(String.format(Locale.FRANCE, "%.2f", amount * 1.0)); // convertir en décimal affichage

        TextWatcher watcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { }
            @Override
            public void afterTextChanged(Editable s) {
                updateTotal();
            }
        };

        donInput.addTextChangedListener(watcher);
        contributionInput.addTextChangedListener(watcher);

        updateTotal();

        payButton.setOnClickListener(v -> {
            if (checkboxUnderstood.isChecked() && checkboxAccepted.isChecked()) {
                proceedPayment();
            } else {
                Toast.makeText(getContext(), "Veuillez cocher les deux cases pour continuer.", Toast.LENGTH_LONG).show();
            }
        });
    }

    private double parseDoubleSafe(EditText editText) {
        try {
            String text = editText.getText().toString().trim();
            if (text.isEmpty()) {
                return 0.0;
            }
            return Double.parseDouble(text.replace(',', '.')); // au cas où utilisateur met une virgule
        } catch (NumberFormatException e) {
            return 0.0;
        }
    }

    private void updateTotal() {
        double don = parseDoubleSafe(donInput);
        double contribution = parseDoubleSafe(contributionInput);
        double total = don + contribution;
        totalInput.setText(String.format(Locale.FRANCE, "%.2f", total));
    }

    private void proceedPayment() {
        double donValue = parseDoubleSafe(donInput);
        double contributionValue = parseDoubleSafe(contributionInput);
        double totalValue = parseDoubleSafe(totalInput);

        long currentTimestamp = System.currentTimeMillis();

        SharedPreferences prefs = requireContext().getSharedPreferences("user_prefs", Context.MODE_PRIVATE);
        int userId = prefs.getInt("id", 0);

        // Attention : ton modèle DonModel prend des int, ici on convertit en centimes (int)
        DonModel don = new DonModel(userId,
                (int)(donValue * 100),
                (int)(contributionValue * 100),
                (int)(totalValue * 100),
                currentTimestamp);

        Log.d("DEBUG_PAYMENT", "Données don envoyées : userId=" + userId +
                ", don=" + don.getMontant() +
                ", contribution=" + don.getContribution() +
                ", total=" + don.getTotal() +
                ", timestamp=" + don.getDate());


        ApiClient apiClient = ApiClient.getInstance(requireContext());
        apiClient.enregistrerDon(don, new ApiClient.ApiCallback<String>() {
            @Override
            public void onSuccess(String response) {
                try {
                    Log.d("DEBUG_PAYMENT", "Réponse brute de l'API : " + response);

                    JSONObject json = new JSONObject(response);
                    boolean success = json.getBoolean("success");
                    if (success) {
                        String message = json.getString("message");
                        String numeroTransaction = json.getString("numero_transaction");

                        Toast.makeText(getContext(), message + "\nN° Transaction : " + numeroTransaction, Toast.LENGTH_LONG).show();

                        // Navigation vers le ticket ou autre logique à ajouter ici
                    } else {
                        Toast.makeText(getContext(), "Erreur serveur : " + json.optString("message"), Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    Toast.makeText(getContext(), "Réponse JSON invalide", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onError(Throwable error) {
                Log.e("API_ERROR", "Erreur lors de l'enregistrement du don", error);
                error.printStackTrace(); // Affiche la trace complète dans Logcat
                Toast.makeText(getContext(), "Erreur lors de l'enregistrement : " + error.getMessage(), Toast.LENGTH_LONG).show();
            }

        });
    }
}
