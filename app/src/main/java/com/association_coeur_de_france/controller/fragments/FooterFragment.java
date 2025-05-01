package com.association_coeur_de_france.controller.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.association_coeur_de_france.R;
import com.association_coeur_de_france.model.FooterModel;

public class FooterFragment extends Fragment {

    private FooterModel model;

    public FooterFragment() {
        // Obligatoire pour Fragment
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_footer, container, false);
        Log.d("FooterFragment", "Footer fragment loaded");
        return rootView;
    }
}
