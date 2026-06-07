package com.bibliotheque.dao;

import com.bibliotheque.model.Utilisateur;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

/**
 * Acces aux donnees des utilisateurs (authentification).
 */
public class UtilisateurDAO {

    /**
     * Recherche un utilisateur par email.
     */
    public Optional<Utilisateur> findByEmail(String email) throws SQLException {
        String sql = "SELECT id, email, mot_de_passe_hash, role, nom, prenom FROM utilisateurs WHERE email = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, email);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Utilisateur u = new Utilisateur();
                    u.setId(rs.getInt("id"));
                    u.setEmail(rs.getString("email"));
                    u.setMotDePasseHash(rs.getString("mot_de_passe_hash"));
                    u.setRole(rs.getString("role"));
                    u.setNom(rs.getString("nom"));
                    u.setPrenom(rs.getString("prenom"));
                    return Optional.of(u);
                }
            }
        }
        return Optional.empty();
    }
}
