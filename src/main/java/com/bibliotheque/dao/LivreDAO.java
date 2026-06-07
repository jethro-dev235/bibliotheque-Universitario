package com.bibliotheque.dao;

import com.bibliotheque.model.Livre;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Acces aux donnees des livres.
 */
public class LivreDAO {

    /**
     * Liste tous les livres.
     */
    public List<Livre> findAll() throws SQLException {
        String sql = "SELECT id, titre, auteur, isbn, categorie, annee_publication, nb_exemplaires, nb_disponibles FROM livres ORDER BY titre";
        List<Livre> list = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(mapRow(rs));
            }
        }
        return list;
    }

    /**
     * Recherche multicritere sur les livres.
     */
    public List<Livre> search(String titre, String auteur, String isbn, String categorie) throws SQLException {
        StringBuilder sql = new StringBuilder(
                "SELECT id, titre, auteur, isbn, categorie, annee_publication, nb_exemplaires, nb_disponibles FROM livres WHERE 1=1");
        List<String> params = new ArrayList<>();
        if (titre != null && !titre.isBlank()) {
            sql.append(" AND titre LIKE ?");
            params.add("%" + titre.trim() + "%");
        }
        if (auteur != null && !auteur.isBlank()) {
            sql.append(" AND auteur LIKE ?");
            params.add("%" + auteur.trim() + "%");
        }
        if (isbn != null && !isbn.isBlank()) {
            sql.append(" AND isbn LIKE ?");
            params.add("%" + isbn.trim() + "%");
        }
        if (categorie != null && !categorie.isBlank()) {
            sql.append(" AND categorie LIKE ?");
            params.add("%" + categorie.trim() + "%");
        }
        sql.append(" ORDER BY titre");
        List<Livre> list = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql.toString())) {
            for (int i = 0; i < params.size(); i++) {
                ps.setString(i + 1, params.get(i));
            }
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapRow(rs));
                }
            }
        }
        return list;
    }

    /**
     * Trouve un livre par identifiant.
     */
    public Optional<Livre> findById(int id) throws SQLException {
        String sql = "SELECT id, titre, auteur, isbn, categorie, annee_publication, nb_exemplaires, nb_disponibles FROM livres WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapRow(rs));
                }
            }
        }
        return Optional.empty();
    }

    /**
     * Insere un nouveau livre.
     */
    public int insert(Livre livre) throws SQLException {
        String sql = "INSERT INTO livres (titre, auteur, isbn, categorie, annee_publication, nb_exemplaires, nb_disponibles) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, livre.getTitre());
            ps.setString(2, livre.getAuteur());
            ps.setString(3, livre.getIsbn());
            ps.setString(4, livre.getCategorie());
            ps.setInt(5, livre.getAnneePublication());
            ps.setInt(6, livre.getNbExemplaires());
            ps.setInt(7, livre.getNbDisponibles());
            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) {
                    return keys.getInt(1);
                }
            }
        }
        return -1;
    }

    /**
     * Met a jour un livre existant.
     */
    public void update(Livre livre) throws SQLException {
        String sql = "UPDATE livres SET titre=?, auteur=?, isbn=?, categorie=?, annee_publication=?, nb_exemplaires=?, nb_disponibles=? WHERE id=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, livre.getTitre());
            ps.setString(2, livre.getAuteur());
            ps.setString(3, livre.getIsbn());
            ps.setString(4, livre.getCategorie());
            ps.setInt(5, livre.getAnneePublication());
            ps.setInt(6, livre.getNbExemplaires());
            ps.setInt(7, livre.getNbDisponibles());
            ps.setInt(8, livre.getId());
            ps.executeUpdate();
        }
    }

    /**
     * Supprime un livre par id.
     */
    public void delete(int id) throws SQLException {
        String sql = "DELETE FROM livres WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }

    /**
     * Compte le nombre total de livres.
     */
    public int count() throws SQLException {
        String sql = "SELECT COUNT(*) FROM livres";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                return rs.getInt(1);
            }
        }
        return 0;
    }

    /**
     * Decremente le stock disponible d'un exemplaire.
     */
    public boolean decrementDisponible(int idLivre) throws SQLException {
        String sql = "UPDATE livres SET nb_disponibles = nb_disponibles - 1 WHERE id = ? AND nb_disponibles > 0";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idLivre);
            return ps.executeUpdate() > 0;
        }
    }

    /**
     * Incremente le stock disponible d'un exemplaire.
     */
    public void incrementDisponible(int idLivre) throws SQLException {
        String sql = "UPDATE livres SET nb_disponibles = nb_disponibles + 1 WHERE id = ? AND nb_disponibles < nb_exemplaires";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idLivre);
            ps.executeUpdate();
        }
    }

    private Livre mapRow(ResultSet rs) throws SQLException {
        return new Livre(
                rs.getInt("id"),
                rs.getString("titre"),
                rs.getString("auteur"),
                rs.getString("isbn"),
                rs.getString("categorie"),
                rs.getInt("annee_publication"),
                rs.getInt("nb_exemplaires"),
                rs.getInt("nb_disponibles")
        );
    }
}
