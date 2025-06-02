package com.association_coeur_de_france.network;

public interface LoginCallback {
    void onSuccess(int id, String firstName, String lastName, String email, String token,
                   String birthdate, String address, String postalCode,
                   String city, String country, String createdAt);
    void onError(String message);
}
