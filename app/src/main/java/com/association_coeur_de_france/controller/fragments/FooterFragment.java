package com.association_coeur_de_france.controller.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.association_coeur_de_france.R;

public class FooterFragment extends Fragment {

    public interface OnFooterButtonClickListener {
        void onFooterButtonClick(int buttonId);
    }

    private OnFooterButtonClickListener listener;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof OnFooterButtonClickListener) {
            listener = (OnFooterButtonClickListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFooterButtonClickListener");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_footer, container, false);

        int[] buttonIds = {R.id.btn_accueil, R.id.btn_adhesion, R.id.btn_message, R.id.btn_profil, R.id.btn_mouvement};
        for (int id : buttonIds) {
            rootView.findViewById(id).setOnClickListener(v -> listener.onFooterButtonClick(id));
        }
        return rootView;
    }
}
