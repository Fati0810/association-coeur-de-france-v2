package com.association_coeur_de_france.model;

public class DonModel {
    private int userId;
    private int montant;
    private int contribution;
    private int total;
    private long date; // timestamp en millisecondes

    // Constructeur complet
    public DonModel(int userId, int montant, int contribution, int total, long date) {
        this.userId = userId;
        this.montant = montant;
        this.contribution = contribution;
        this.total = total;
        this.date = date;
    }

    // Getters
    public int getUserId() {
        return userId;
    }

    public int getMontant() {
        return montant;
    }

    public int getContribution() {
        return contribution;
    }

    public int getTotal() {
        return total;
    }

    public long getDate() {
        return date;
    }

    // Setters
    public void setUserId(int userId) {
        this.userId = userId;
    }

    public void setMontant(int montant) {
        this.montant = montant;
    }

    public void setContribution(int contribution) {
        this.contribution = contribution;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public void setDate(long date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "DonModel{" +
                "userId=" + userId +
                ", montant=" + montant +
                ", contribution=" + contribution +
                ", total=" + total +
                ", date=" + date +
                '}';
    }
}
