package com.bibliotheque.model;

import java.sql.Date;
import java.time.LocalDate;

/**
 * Entite representant une penalite de retard.
 */
public class Penalite {

    private int id;
    private int idEmprunt;
    private double montant;
    private LocalDate dateCalcul;
    private String statutPaiement;
    private String titreLivre;
    private String nomAbonne;
    private int idAbonne;

    public Penalite() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdEmprunt() {
        return idEmprunt;
    }

    public void setIdEmprunt(int idEmprunt) {
        this.idEmprunt = idEmprunt;
    }

    public double getMontant() {
        return montant;
    }

    public void setMontant(double montant) {
        this.montant = montant;
    }

    public LocalDate getDateCalcul() {
        return dateCalcul;
    }

    public void setDateCalcul(LocalDate dateCalcul) {
        this.dateCalcul = dateCalcul;
    }

    public String getStatutPaiement() {
        return statutPaiement;
    }

    public void setStatutPaiement(String statutPaiement) {
        this.statutPaiement = statutPaiement;
    }

    public String getTitreLivre() {
        return titreLivre;
    }

    public void setTitreLivre(String titreLivre) {
        this.titreLivre = titreLivre;
    }

    public String getNomAbonne() {
        return nomAbonne;
    }

    public void setNomAbonne(String nomAbonne) {
        this.nomAbonne = nomAbonne;
    }

    public int getIdAbonne() {
        return idAbonne;
    }

    public void setIdAbonne(int idAbonne) {
        this.idAbonne = idAbonne;
    }

    /** Date formatee JSP (fmt:formatDate). */
    public Date getDateCalculFmt() {
        return dateCalcul == null ? null : Date.valueOf(dateCalcul);
    }
}
