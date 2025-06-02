package com.association_coeur_de_france.controller.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
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

public class RecapitulatifFragment extends Fragment {
    // chatgpt : Centrage du texte, finir la page de l'api, j'envoie les infos de l'utilisateur connecter, le montant du don la contribution le total
    // le don et la transaction sont stockées
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

        // Récupérer le montant depuis SharedPreferences
        SharedPreferences prefs = requireContext().getSharedPreferences("don_prefs", Context.MODE_PRIVATE);
        int amount = prefs.getInt("montant_don", 0);
        donInput.setText(String.valueOf(amount));

        // Ajouter des TextWatcher pour recalculer la somme à chaque changement
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

        // Calcul initial
        updateTotal();

        payButton.setOnClickListener(v -> {
            if (checkboxUnderstood.isChecked() && checkboxAccepted.isChecked()) {
                // Les deux cases sont cochées : procéder au paiement
                proceedPayment();
            } else {
                // Sinon, afficher un message d'erreur
                Toast.makeText(getContext(), "Veuillez cocher les deux cases pour continuer.", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void updateTotal() {
        int donValue = parseIntSafe(donInput.getText().toString());
        int contributionValue = parseIntSafe(contributionInput.getText().toString());

        int total = donValue + contributionValue;
        totalInput.setText(String.valueOf(total));
    }

    private int parseIntSafe(String s) {
        try {
            return Integer.parseInt(s.trim());
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    private void proceedPayment() {
        // TODO : Ajoute ici le code pour lancer le paiement ou la prochaine étape
        Toast.makeText(getContext(), "Paiement en cours...", Toast.LENGTH_SHORT).show();
    }
}
