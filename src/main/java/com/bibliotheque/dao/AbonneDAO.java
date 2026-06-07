package com.bibliotheque.dao;

import com.bibliotheque.model.Abonne;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Acces aux donnees des abonnes.
 */
public class AbonneDAO {

    /**
     * Liste tous les abonnes.
     */
    public List<Abonne> findAll() throws SQLException {
        String sql = "SELECT id, nom, prenom, email, telephone, date_inscription, statut FROM abonnes ORDER BY nom, prenom";
        List<Abonne> list = new ArrayList<>();
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
     * Liste les abonnes actifs uniquement.
     */
    public List<Abonne> findActifs() throws SQLException {
        String sql = "SELECT id, nom, prenom, email, telephone, date_inscription, statut FROM abonnes WHERE statut = 'ACTIF' ORDER BY nom, prenom";
        List<Abonne> list = new ArrayList<>();
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
     * Trouve un abonne par id.
     */
    public Optional<Abonne> findById(int id) throws SQLException {
        String sql = "SELECT id, nom, prenom, email, telephone, date_inscription, statut FROM abonnes WHERE id = ?";
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
     * Insere un nouvel abonne.
     */
    public int insert(Abonne abonne) throws SQLException {
        String sql = "INSERT INTO abonnes (nom, prenom, email, telephone, date_inscription, statut) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, abonne.getNom());
            ps.setString(2, abonne.getPrenom());
            ps.setString(3, abonne.getEmail());
            ps.setString(4, abonne.getTelephone());
            ps.setDate(5, Date.valueOf(abonne.getDateInscription()));
            ps.setString(6, abonne.getStatut());
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
     * Met a jour un abonne.
     */
    public void update(Abonne abonne) throws SQLException {
        String sql = "UPDATE abonnes SET nom=?, prenom=?, email=?, telephone=?, date_inscription=?, statut=? WHERE id=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, abonne.getNom());
            ps.setString(2, abonne.getPrenom());
            ps.setString(3, abonne.getEmail());
            ps.setString(4, abonne.getTelephone());
            ps.setDate(5, Date.valueOf(abonne.getDateInscription()));
            ps.setString(6, abonne.getStatut());
            ps.setInt(7, abonne.getId());
            ps.executeUpdate();
        }
    }

    /**
     * Supprime un abonne.
     */
    public void delete(int id) throws SQLException {
        String sql = "DELETE FROM abonnes WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }

    /**
     * Suspend un abonne.
     */
    public void suspendre(int id) throws SQLException {
        updateStatut(id, "SUSPENDU");
    }

    /**
     * Reactive un abonne.
     */
    public void reactiver(int id) throws SQLException {
        updateStatut(id, "ACTIF");
    }

    private void updateStatut(int id, String statut) throws SQLException {
        String sql = "UPDATE abonnes SET statut = ? WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, statut);
            ps.setInt(2, id);
            ps.executeUpdate();
        }
    }

    /**
     * Compte les abonnes actifs.
     */
    public int countActifs() throws SQLException {
        String sql = "SELECT COUNT(*) FROM abonnes WHERE statut = 'ACTIF'";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                return rs.getInt(1);
            }
        }
        return 0;
    }

    private Abonne mapRow(ResultSet rs) throws SQLException {
        Date d = rs.getDate("date_inscription");
        LocalDate dateInscription = d != null ? d.toLocalDate() : null;
        return new Abonne(
                rs.getInt("id"),
                rs.getString("nom"),
                rs.getString("prenom"),
                rs.getString("email"),
                rs.getString("telephone"),
                dateInscription,
                rs.getString("statut")
        );
    }
}
