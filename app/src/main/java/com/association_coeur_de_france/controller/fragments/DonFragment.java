package com.association_coeur_de_france.controller.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.association_coeur_de_france.R;
import com.association_coeur_de_france.controller.MainActivity;
import com.association_coeur_de_france.model.DonModel;

public class DonFragment extends Fragment {

    private EditText etCustomAmount;
    private int selectedAmount = 0;

    public DonFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_don, container, false);

        etCustomAmount = view.findViewById(R.id.etCustomAmount);
        Button btnDonate = view.findViewById(R.id.btnDonate);

        Button btn5 = view.findViewById(R.id.button_amount_5);
        Button btn10 = view.findViewById(R.id.button_amount_10);
        Button btn15 = view.findViewById(R.id.button_amount_15);
        Button btn20 = view.findViewById(R.id.button_amount_20);

        // Listeners pour les montants fixes
        btn5.setOnClickListener(v -> selectAmount(5));
        btn10.setOnClickListener(v -> selectAmount(10));
        btn15.setOnClickListener(v -> selectAmount(15));
        btn20.setOnClickListener(v -> selectAmount(20));

        // Listener du bouton DON
        btnDonate.setOnClickListener(v -> {
            int montant = getMontantSelectionne();
            if (montant <= 0) {
                Toast.makeText(getContext(), "Veuillez choisir ou saisir un montant.", Toast.LENGTH_SHORT).show();
                return;
            }

            // Enregistrer le montant dans les SharedPreferences
            SharedPreferences prefs = requireActivity().getSharedPreferences("don_prefs", Context.MODE_PRIVATE);
            prefs.edit().putInt("montant_don", montant).apply();

            // Passer au fragment récapitulatif
            InfosFragment infosFragment = new InfosFragment();
            ((MainActivity) requireActivity()).loadFragment(infosFragment);
        });

        return view;
    }

    private int getMontantSelectionne() {
        String customText = etCustomAmount.getText().toString().trim();
        if (!customText.isEmpty()) {
            try {
                return Integer.parseInt(customText);
            } catch (NumberFormatException e) {
                Toast.makeText(getContext(), "Veuillez entrer un montant valide.", Toast.LENGTH_SHORT).show();
                return -1;
            }
        } else {
            return selectedAmount > 0 ? selectedAmount : -1;
        }
    }


    // Gère la sélection d'un bouton de montant fixe
    private void selectAmount(int amount) {
        selectedAmount = amount;
        etCustomAmount.setText(String.valueOf(amount)); // Affiche le montant dans le champ
        Toast.makeText(getContext(), "Montant sélectionné : " + amount + "€", Toast.LENGTH_SHORT).show();
    }
}

