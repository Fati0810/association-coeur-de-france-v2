package com.association_coeur_de_france.controller.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.association_coeur_de_france.R;
import com.association_coeur_de_france.SessionManager;

public class HomeFragment extends Fragment {

    private TextView welcomeText;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        welcomeText = view.findViewById(R.id.welcome_text);

        // Utilisation de SessionManager pour récupérer le prénom
        SessionManager sessionManager = new SessionManager(requireContext());
        String firstName = sessionManager.getFirstName();

        if (firstName == null || firstName.isEmpty()) {
            firstName = "Utilisateur";
        }

        welcomeText.setText("Bonjour " + firstName + " !");

        return view;
    }
}
