package com.association_coeur_de_france;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.security.crypto.EncryptedSharedPreferences;
import androidx.security.crypto.MasterKeys;

import java.io.IOException;
import java.security.GeneralSecurityException;

public class SessionManager {

    private static final String PREFS_NAME = "secure_user_session";

    private SharedPreferences sharedPreferences;

    public SessionManager(Context context) {
        try {
            String masterKeyAlias = MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC);

            sharedPreferences = EncryptedSharedPreferences.create(
                    PREFS_NAME,
                    masterKeyAlias,
                    context,
                    EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                    EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
            );

        } catch (GeneralSecurityException | IOException e) {
            e.printStackTrace();
            // En cas d’erreur, on peut fallback sur SharedPreferences simple (optionnel)
            sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        }
    }

    public void saveSession(int id, String firstName, String lastName, String email, String token,
                            String birthdate, String address, String postalCode,
                            String city, String country, String createdAt) {
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putInt("id", id);
        editor.putString("first_name", firstName);
        editor.putString("last_name", lastName);
        editor.putString("email", email);
        editor.putString("token", token);
        editor.putString("birthdate", birthdate);
        editor.putString("address", address);
        editor.putString("postal_code", postalCode);
        editor.putString("city", city);
        editor.putString("country", country);
        editor.putString("created_at", createdAt);

        editor.apply();
    }

    // Exemple : récupérer le token
    public String getToken() {
        return sharedPreferences.getString("token", null);
    }

    public int getId() {
        return sharedPreferences.getInt("id", -1);
    }


    // Exemple : récupérer le prénom
    public String getFirstName() {
        return sharedPreferences.getString("first_name", null);
    }

    // Ajouter d’autres getters selon besoin...

    // Supprimer la session (déconnexion)
    public void clearSession() {
        sharedPreferences.edit().clear().apply();
    }

    // Vérifier si l’utilisateur est connecté (token non null)
    public boolean isLoggedIn() {
        return getToken() != null;
    }


}
