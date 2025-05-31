package com.association_coeur_de_france.network;

public interface LoginCallback {
    void onSuccess(String email, String token);
    void onError(String message);
}
