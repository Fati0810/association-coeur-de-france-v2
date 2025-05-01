package com.association_coeur_de_france.controller;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.association_coeur_de_france.R;
import com.association_coeur_de_france.model.HeaderModel;

public class HeaderFragment extends Fragment {

    private HeaderModel model;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_header, container, false);

        // Modèle contenant la ressource du logo
        model = new HeaderModel(R.drawable.logo_c2f); // remplace ic_logo par ton image

        ImageView logoImage = view.findViewById(R.id.logoImage);
        logoImage.setImageResource(model.getLogoResId());

        return view;
    }
}
