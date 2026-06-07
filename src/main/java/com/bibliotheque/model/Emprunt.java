package com.bibliotheque.model;

import java.sql.Date;
import java.time.LocalDate;

/**
 * Entite representant un emprunt de livre.
 */
public class Emprunt {

    private int id;
    private int idLivre;
    private int idAbonne;
    private LocalDate dateEmprunt;
    private LocalDate dateRetourPrevue;
    private LocalDate dateRetourEffective;
    private String statut;
    private String titreLivre;
    private String nomAbonne;
    private long joursRetard;
    private double montantPenalite;

    public Emprunt() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdLivre() {
        return idLivre;
    }

    public void setIdLivre(int idLivre) {
        this.idLivre = idLivre;
    }

    public int getIdAbonne() {
        return idAbonne;
    }

    public void setIdAbonne(int idAbonne) {
        this.idAbonne = idAbonne;
    }

    public LocalDate getDateEmprunt() {
        return dateEmprunt;
    }

    public void setDateEmprunt(LocalDate dateEmprunt) {
        this.dateEmprunt = dateEmprunt;
    }

    public LocalDate getDateRetourPrevue() {
        return dateRetourPrevue;
    }

    public void setDateRetourPrevue(LocalDate dateRetourPrevue) {
        this.dateRetourPrevue = dateRetourPrevue;
    }

    public LocalDate getDateRetourEffective() {
        return dateRetourEffective;
    }

    public void setDateRetourEffective(LocalDate dateRetourEffective) {
        this.dateRetourEffective = dateRetourEffective;
    }

    public String getStatut() {
        return statut;
    }

    public void setStatut(String statut) {
        this.statut = statut;
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

    public long getJoursRetard() {
        return joursRetard;
    }

    public void setJoursRetard(long joursRetard) {
        this.joursRetard = joursRetard;
    }

    public double getMontantPenalite() {
        return montantPenalite;
    }

    public void setMontantPenalite(double montantPenalite) {
        this.montantPenalite = montantPenalite;
    }

    public boolean isEnRetard() {
        return "EN_RETARD".equals(statut)
                || ("EN_COURS".equals(statut) && dateRetourPrevue != null
                && dateRetourPrevue.isBefore(LocalDate.now()));
    }

    /** Date formatee JSP (fmt:formatDate). */
    public Date getDateEmpruntFmt() {
        return dateEmprunt == null ? null : Date.valueOf(dateEmprunt);
    }

    /** Date formatee JSP (fmt:formatDate). */
    public Date getDateRetourPrevueFmt() {
        return dateRetourPrevue == null ? null : Date.valueOf(dateRetourPrevue);
    }

    /** Date formatee JSP (fmt:formatDate). */
    public Date getDateRetourEffectiveFmt() {
        return dateRetourEffective == null ? null : Date.valueOf(dateRetourEffective);
    }
}
