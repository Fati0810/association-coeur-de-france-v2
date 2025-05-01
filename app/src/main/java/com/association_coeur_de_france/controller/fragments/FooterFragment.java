package com.association_coeur_de_france.controller;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.association_coeur_de_france.R;
import com.association_coeur_de_france.model.FooterModel;

public class FooterFragment extends Fragment {

    private FooterModel model;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_footer, container, false);

        // Initialiser le modèle
        model = new FooterModel("Bienvenue dans le footer de l'application");

        // Remplir la vue avec les données du modèle
        TextView footerText = view.findViewById(R.id.footerText);
        footerText.setText(model.getFooterText());

        return view;
    }
}
