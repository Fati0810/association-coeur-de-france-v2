package com.association_coeur_de_france.controller.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.association_coeur_de_france.R;
import com.association_coeur_de_france.controller.MainActivity;

public class ConfirmationFragment extends Fragment {

    public ConfirmationFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_confirmation, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view,
                              @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Button buttonAccueil = view.findViewById(R.id.buttonAccueil);

        buttonAccueil.setOnClickListener(v -> {
            if (getActivity() != null && getActivity() instanceof MainActivity) {
                ((MainActivity) getActivity()).loadFragment(new HomeFragment());
            }
        });
    }
}
