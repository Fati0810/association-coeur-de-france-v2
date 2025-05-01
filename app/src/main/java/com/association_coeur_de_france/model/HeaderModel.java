package com.association_coeur_de_france.model;

public class HeaderModel {
    private int logoResId;

    public HeaderModel(int logoResId) {
        this.logoResId = logoResId;
    }

    public int getLogoResId() {
        return logoResId;
    }

    public void setLogoResId(int logoResId) {
        this.logoResId = logoResId;
    }
}
