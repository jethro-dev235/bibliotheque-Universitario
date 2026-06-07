package com.bibliotheque.dao;

import com.bibliotheque.model.Emprunt;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Acces aux donnees des emprunts.
 */
public class EmpruntDAO {

    private static final double PENALITE_PAR_JOUR = 50.0;

    private static final String SELECT_BASE =
            "SELECT e.id, e.id_livre, e.id_abonne, e.date_emprunt, e.date_retour_prevue, e.date_retour_effective, e.statut, "
                    + "l.titre AS titre_livre, CONCAT(a.prenom, ' ', a.nom) AS nom_abonne "
                    + "FROM emprunts e "
                    + "JOIN livres l ON e.id_livre = l.id "
                    + "JOIN abonnes a ON e.id_abonne = a.id ";

    /**
     * Met a jour les statuts en retard avant lecture.
     */
    public void synchroniserRetards() throws SQLException {
        String sql = "UPDATE emprunts SET statut = 'EN_RETARD' WHERE statut = 'EN_COURS' AND date_retour_prevue < CURDATE()";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.executeUpdate();
        }
        penaliteDAO().genererPenalitesRetard();
    }

    private PenaliteDAO penaliteDAO() {
        return new PenaliteDAO();
    }

    /**
     * Liste tous les emprunts.
     */
    public List<Emprunt> findAll() throws SQLException {
        synchroniserRetards();
        return queryList(SELECT_BASE + "ORDER BY e.date_emprunt DESC");
    }

    /**
     * Liste les emprunts en cours.
     */
    public List<Emprunt> findEnCours() throws SQLException {
        synchroniserRetards();
        return queryList(SELECT_BASE + "WHERE e.statut IN ('EN_COURS', 'EN_RETARD') ORDER BY e.date_retour_prevue");
    }

    /**
     * Liste les emprunts en retard.
     */
    public List<Emprunt> findEnRetard() throws SQLException {
        synchroniserRetards();
        return queryList(SELECT_BASE + "WHERE e.statut = 'EN_RETARD' ORDER BY e.date_retour_prevue");
    }

    /**
     * Historique des emprunts d'un abonne.
     */
    public List<Emprunt> findByAbonne(int idAbonne) throws SQLException {
        synchroniserRetards();
        String sql = SELECT_BASE + "WHERE e.id_abonne = ? ORDER BY e.date_emprunt DESC";
        List<Emprunt> list = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idAbonne);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapRow(rs));
                }
            }
        }
        return list;
    }

    /**
     * Trouve un emprunt par id.
     */
    public Optional<Emprunt> findById(int id) throws SQLException {
        synchroniserRetards();
        String sql = SELECT_BASE + "WHERE e.id = ?";
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
     * Cree un nouvel emprunt.
     */
    public int insert(int idLivre, int idAbonne, LocalDate dateEmprunt, LocalDate dateRetourPrevue) throws SQLException {
        String sql = "INSERT INTO emprunts (id_livre, id_abonne, date_emprunt, date_retour_prevue, statut) VALUES (?, ?, ?, ?, 'EN_COURS')";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, idLivre);
            ps.setInt(2, idAbonne);
            ps.setDate(3, Date.valueOf(dateEmprunt));
            ps.setDate(4, Date.valueOf(dateRetourPrevue));
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
     * Enregistre le retour d'un emprunt.
     */
    public void enregistrerRetour(int idEmprunt, LocalDate dateRetour) throws SQLException {
        String sql = "UPDATE emprunts SET date_retour_effective = ?, statut = 'RETOURNE' WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setDate(1, Date.valueOf(dateRetour));
            ps.setInt(2, idEmprunt);
            ps.executeUpdate();
        }
    }

    /**
     * Compte les emprunts en cours.
     */
    public int countEnCours() throws SQLException {
        synchroniserRetards();
        String sql = "SELECT COUNT(*) FROM emprunts WHERE statut IN ('EN_COURS', 'EN_RETARD')";
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
     * Compte les emprunts en retard.
     */
    public int countRetards() throws SQLException {
        synchroniserRetards();
        String sql = "SELECT COUNT(*) FROM emprunts WHERE statut = 'EN_RETARD'";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                return rs.getInt(1);
            }
        }
        return 0;
    }

    private List<Emprunt> queryList(String sql) throws SQLException {
        List<Emprunt> list = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(mapRow(rs));
            }
        }
        return list;
    }

    private Emprunt mapRow(ResultSet rs) throws SQLException {
        Emprunt e = new Emprunt();
        e.setId(rs.getInt("id"));
        e.setIdLivre(rs.getInt("id_livre"));
        e.setIdAbonne(rs.getInt("id_abonne"));
        Date de = rs.getDate("date_emprunt");
        Date drp = rs.getDate("date_retour_prevue");
        Date dre = rs.getDate("date_retour_effective");
        e.setDateEmprunt(de != null ? de.toLocalDate() : null);
        e.setDateRetourPrevue(drp != null ? drp.toLocalDate() : null);
        e.setDateRetourEffective(dre != null ? dre.toLocalDate() : null);
        e.setStatut(rs.getString("statut"));
        e.setTitreLivre(rs.getString("titre_livre"));
        e.setNomAbonne(rs.getString("nom_abonne"));
        if (e.getDateRetourPrevue() != null && e.getDateRetourEffective() == null
                && e.getDateRetourPrevue().isBefore(LocalDate.now())) {
            long jours = ChronoUnit.DAYS.between(e.getDateRetourPrevue(), LocalDate.now());
            e.setJoursRetard(jours);
            e.setMontantPenalite(jours * PENALITE_PAR_JOUR);
        }
        return e;
    }
}
