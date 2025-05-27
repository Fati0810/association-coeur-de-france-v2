package com.association_coeur_de_france.model;

public class UserModel {
    private String firstName;
    private String lastName;
    private String email;
    private String emailConfirmation;
    private String birthdate; // Peut être transformé en Date selon les besoins
    private String address;
    private String postalCode;
    private String city;
    private String country;

    public UserModel() {
        // Constructeur vide requis par certains frameworks
    }

    public UserModel(String firstName, String lastName, String email, String emailConfirmation,
                     String birthdate, String address, String postalCode, String city, String country) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.emailConfirmation = emailConfirmation;
        this.birthdate = birthdate;
        this.address = address;
        this.postalCode = postalCode;
        this.city = city;
        this.country = country;
    }

    // Getters et setters

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEmailConfirmation() {
        return emailConfirmation;
    }

    public void setEmailConfirmation(String emailConfirmation) {
        this.emailConfirmation = emailConfirmation;
    }

    public String getBirthdate() {
        return birthdate;
    }

    public void setBirthdate(String birthdate) {
        this.birthdate = birthdate;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }
}
