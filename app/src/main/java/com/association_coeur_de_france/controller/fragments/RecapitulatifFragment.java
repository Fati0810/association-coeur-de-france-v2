package com.association_coeur_de_france.controller.fragments;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.association_coeur_de_france.R;

public class RecapitulatifFragment extends Fragment {

    private EditText donInput;
    private EditText contributionInput;
    private EditText totalInput;

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

        // Récupérer le montant envoyé depuis DonFragment (si présent)
        Bundle args = getArguments();
        if (args != null && args.containsKey("selected_amount")) {
            int amount = args.getInt("selected_amount");
            donInput.setText(String.valueOf(amount));
        }

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
}
