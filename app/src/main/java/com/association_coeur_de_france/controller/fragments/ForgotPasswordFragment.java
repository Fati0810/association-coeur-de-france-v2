package com.association_coeur_de_france.controller.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.association_coeur_de_france.R;
import com.association_coeur_de_france.controller.MainActivity;
import com.association_coeur_de_france.network.ApiClient;

public class ForgotPasswordFragment extends Fragment {

    private EditText emailEditText;
    private Button sendButton;
    private ProgressBar progressBar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_forgot_password, container, false);

        emailEditText = view.findViewById(R.id.editTextEmail);
        sendButton = view.findViewById(R.id.buttonSend);
        progressBar = view.findViewById(R.id.progressBar);

        sendButton.setOnClickListener(v -> {
            String email = emailEditText.getText().toString().trim();

            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                Toast.makeText(getContext(), "Veuillez entrer un email valide", Toast.LENGTH_SHORT).show();
                return;
            }

            sendButton.setEnabled(false);
            progressBar.setVisibility(View.VISIBLE);

            ApiClient.getInstance(requireContext()).resetPassword(email, new ApiClient.ResetPasswordCallback() {
                @Override
                public void onSuccess(String message) {
                    Toast.makeText(getContext(), "Un email de réinitialisation a été envoyé.", Toast.LENGTH_LONG).show();
                    emailEditText.setText("");
                    sendButton.setEnabled(true);
                    progressBar.setVisibility(View.GONE);

                    if (getActivity() != null && getActivity() instanceof MainActivity) {
                        ((MainActivity) getActivity()).loadFragment(new LoginFragment());
                    }
                }

                @Override
                public void onError(String errorMessage) {
                    Toast.makeText(getContext(), errorMessage, Toast.LENGTH_SHORT).show();
                    sendButton.setEnabled(true);
                    progressBar.setVisibility(View.GONE);
                }
            });
        });

        return view;
    }

}
