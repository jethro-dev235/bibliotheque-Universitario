package com.bibliotheque.model;

import java.sql.Date;
import java.time.LocalDate;

/**
 * Entite representant un abonne de la bibliotheque.
 */
public class Abonne {

    private int id;
    private String nom;
    private String prenom;
    private String email;
    private String telephone;
    private LocalDate dateInscription;
    private String statut;

    public Abonne() {
    }

    public Abonne(int id, String nom, String prenom, String email, String telephone,
                  LocalDate dateInscription, String statut) {
        this.id = id;
        this.nom = nom;
        this.prenom = prenom;
        this.email = email;
        this.telephone = telephone;
        this.dateInscription = dateInscription;
        this.statut = statut;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public LocalDate getDateInscription() {
        return dateInscription;
    }

    public void setDateInscription(LocalDate dateInscription) {
        this.dateInscription = dateInscription;
    }

    public String getStatut() {
        return statut;
    }

    public void setStatut(String statut) {
        this.statut = statut;
    }

    public String getNomComplet() {
        return prenom + " " + nom;
    }

    /** Date formatee JSP (fmt:formatDate). */
    public Date getDateInscriptionFmt() {
        return dateInscription == null ? null : Date.valueOf(dateInscription);
    }
}
