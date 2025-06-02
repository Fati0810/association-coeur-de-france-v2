package com.association_coeur_de_france.controller.fragments;

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

    private int selectedAmount = 0; // Par défaut aucun montant fixe sélectionné

    public DonFragment() {
        // Constructeur vide requis
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

            // Préparer et passer le fragment récapitulatif avec le montant
            RecapitulatifFragment recapFragment = new RecapitulatifFragment();
            Bundle bundle = new Bundle();
            bundle.putInt("selected_amount", montant);
            recapFragment.setArguments(bundle);

            // Remplacer le fragment actuel par le fragment récapitulatif
            if (getActivity() instanceof MainActivity) {
                ((MainActivity) getActivity()).loadFragment(recapFragment);
            }
        });

        return view;
    }

    // Méthode pour récupérer le montant (fixe ou saisi)
    private int getMontantSelectionne() {
        String customText = etCustomAmount.getText().toString().trim();
        if (!customText.isEmpty()) {
            try {
                return Integer.parseInt(customText);
            } catch (NumberFormatException e) {
                Toast.makeText(getContext(), "Veuillez entrer un montant valide.", Toast.LENGTH_SHORT).show();
                return -1;
            }
        } else if (selectedAmount > 0) {
            return selectedAmount;
        } else {
            return -1;
        }
    }


    private void selectAmount(int amount) {
        selectedAmount = amount;
        etCustomAmount.setText(""); // On vide le champ si un montant fixe est sélectionné
        Toast.makeText(getContext(), "Montant sélectionné : " + amount + "€", Toast.LENGTH_SHORT).show();
    }

    private void processDonation() {
        int montant;

        // Si l'utilisateur a saisi un montant personnalisé
        String customText = etCustomAmount.getText().toString().trim();
        if (!customText.isEmpty()) {
            try {
                montant = Integer.parseInt(customText);
            } catch (NumberFormatException e) {
                Toast.makeText(getContext(), "Veuillez entrer un montant valide.", Toast.LENGTH_SHORT).show();
                return;
            }
        } else if (selectedAmount > 0) {
            montant = selectedAmount;
        } else {
            Toast.makeText(getContext(), "Veuillez choisir ou saisir un montant.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Création du don
        DonModel don = new DonModel(montant);
        Toast.makeText(getContext(), "Merci pour votre don de " + don.getMontant() + "€", Toast.LENGTH_LONG).show();

        // Ici, tu pourrais envoyer le don à une API ou une base de données
    }

}
