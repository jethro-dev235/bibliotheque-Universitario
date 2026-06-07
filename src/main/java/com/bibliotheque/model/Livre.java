package com.bibliotheque.model;

/**
 * Entite representant un livre de la bibliotheque.
 */
public class Livre {

    private int id;
    private String titre;
    private String auteur;
    private String isbn;
    private String categorie;
    private int anneePublication;
    private int nbExemplaires;
    private int nbDisponibles;

    public Livre() {
    }

    public Livre(int id, String titre, String auteur, String isbn, String categorie,
                 int anneePublication, int nbExemplaires, int nbDisponibles) {
        this.id = id;
        this.titre = titre;
        this.auteur = auteur;
        this.isbn = isbn;
        this.categorie = categorie;
        this.anneePublication = anneePublication;
        this.nbExemplaires = nbExemplaires;
        this.nbDisponibles = nbDisponibles;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitre() {
        return titre;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public String getAuteur() {
        return auteur;
    }

    public void setAuteur(String auteur) {
        this.auteur = auteur;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getCategorie() {
        return categorie;
    }

    public void setCategorie(String categorie) {
        this.categorie = categorie;
    }

    public int getAnneePublication() {
        return anneePublication;
    }

    public void setAnneePublication(int anneePublication) {
        this.anneePublication = anneePublication;
    }

    public int getNbExemplaires() {
        return nbExemplaires;
    }

    public void setNbExemplaires(int nbExemplaires) {
        this.nbExemplaires = nbExemplaires;
    }

    public int getNbDisponibles() {
        return nbDisponibles;
    }

    public void setNbDisponibles(int nbDisponibles) {
        this.nbDisponibles = nbDisponibles;
    }
}
