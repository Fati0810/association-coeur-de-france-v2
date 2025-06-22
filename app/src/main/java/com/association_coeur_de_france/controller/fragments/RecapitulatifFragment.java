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
import com.association_coeur_de_france.SessionManager;
import com.association_coeur_de_france.controller.MainActivity;

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

        SharedPreferences prefs = requireContext().getSharedPreferences("don_prefs", Context.MODE_PRIVATE);
        int amount = prefs.getInt("montant_don", 0);
        donInput.setText(String.format(Locale.FRANCE, "%.2f", amount * 1.0));

        TextWatcher watcher = new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {}
            @Override public void afterTextChanged(Editable s) { updateTotal(); }
        };

        donInput.addTextChangedListener(watcher);
        contributionInput.addTextChangedListener(watcher);
        updateTotal();

        payButton.setOnClickListener(v -> {
            if (checkboxUnderstood.isChecked() && checkboxAccepted.isChecked()) {
                simulateSuccessAndRedirect();
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
            return Double.parseDouble(text.replace(',', '.'));
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

    private void simulateSuccessAndRedirect() {
        Toast.makeText(getContext(), "Don confirmé avec succès", Toast.LENGTH_LONG).show();

        if (getActivity() != null && getActivity() instanceof MainActivity) {
            MainActivity activity = (MainActivity) getActivity();



            // Puis redirection vers le fragment de confirmation (tu peux aussi mettre un délai si tu veux un effet visuel)
            activity.loadFragment(new ConfirmationFragment());
        }
    }
}
