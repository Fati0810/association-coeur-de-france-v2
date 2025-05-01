package com.association_coeur_de_france.model;

public class FooterModel {
    private int iconResId;
    private String label;

    public FooterModel(int iconResId, String label) {
        this.iconResId = iconResId;
        this.label = label;
    }

    public int getIconResId() {
        return iconResId;
    }

    public String getLabel() {
        return label;
    }
}
