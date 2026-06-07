package com.bibliotheque.dao;

import com.bibliotheque.model.Penalite;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Acces aux donnees des penalites de retard.
 */
public class PenaliteDAO {

    private static final double PENALITE_PAR_JOUR = 50.0;

    /**
     * Genere ou met a jour les penalites pour les emprunts en retard.
     */
    public void genererPenalitesRetard() throws SQLException {
        String selectSql = "SELECT id, date_retour_prevue FROM emprunts WHERE statut = 'EN_RETARD'";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(selectSql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                int idEmprunt = rs.getInt("id");
                LocalDate datePrevue = rs.getDate("date_retour_prevue").toLocalDate();
                long jours = ChronoUnit.DAYS.between(datePrevue, LocalDate.now());
                if (jours <= 0) {
                    continue;
                }
                double montant = jours * PENALITE_PAR_JOUR;
                upsertPenalite(conn, idEmprunt, montant);
            }
        }
    }

    private void upsertPenalite(Connection conn, int idEmprunt, double montant) throws SQLException {
        String check = "SELECT id, statut_paiement FROM penalites WHERE id_emprunt = ?";
        try (PreparedStatement ps = conn.prepareStatement(check)) {
            ps.setInt(1, idEmprunt);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    if ("PAYE".equals(rs.getString("statut_paiement"))) {
                        return;
                    }
                    String update = "UPDATE penalites SET montant = ?, date_calcul = CURDATE() WHERE id_emprunt = ?";
                    try (PreparedStatement ups = conn.prepareStatement(update)) {
                        ups.setDouble(1, montant);
                        ups.setInt(2, idEmprunt);
                        ups.executeUpdate();
                    }
                } else {
                    String insert = "INSERT INTO penalites (id_emprunt, montant, date_calcul, statut_paiement) VALUES (?, ?, CURDATE(), 'IMPAYE')";
                    try (PreparedStatement ins = conn.prepareStatement(insert)) {
                        ins.setInt(1, idEmprunt);
                        ins.setDouble(2, montant);
                        ins.executeUpdate();
                    }
                }
            }
        }
    }

    /**
     * Liste toutes les penalites.
     */
    public List<Penalite> findAll() throws SQLException {
        new EmpruntDAO().synchroniserRetards();
        genererPenalitesRetard();
        String sql = "SELECT p.id, p.id_emprunt, p.montant, p.date_calcul, p.statut_paiement, "
                + "l.titre AS titre_livre, CONCAT(a.prenom, ' ', a.nom) AS nom_abonne, e.id_abonne "
                + "FROM penalites p "
                + "JOIN emprunts e ON p.id_emprunt = e.id "
                + "JOIN livres l ON e.id_livre = l.id "
                + "JOIN abonnes a ON e.id_abonne = a.id "
                + "ORDER BY p.date_calcul DESC";
        List<Penalite> list = new ArrayList<>();
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
     * Marque une penalite comme payee.
     */
    public void marquerPaye(int id) throws SQLException {
        String sql = "UPDATE penalites SET statut_paiement = 'PAYE' WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }

    /**
     * Total impaye par abonne.
     */
    public Map<Integer, Double> totalImpayeParAbonne() throws SQLException {
        genererPenalitesRetard();
        String sql = "SELECT e.id_abonne, SUM(p.montant) AS total "
                + "FROM penalites p JOIN emprunts e ON p.id_emprunt = e.id "
                + "WHERE p.statut_paiement = 'IMPAYE' GROUP BY e.id_abonne";
        Map<Integer, Double> map = new HashMap<>();
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                map.put(rs.getInt("id_abonne"), rs.getDouble("total"));
            }
        }
        return map;
    }

    /**
     * Trouve une penalite par id.
     */
    public Optional<Penalite> findById(int id) throws SQLException {
        String sql = "SELECT p.id, p.id_emprunt, p.montant, p.date_calcul, p.statut_paiement, "
                + "l.titre AS titre_livre, CONCAT(a.prenom, ' ', a.nom) AS nom_abonne, e.id_abonne "
                + "FROM penalites p "
                + "JOIN emprunts e ON p.id_emprunt = e.id "
                + "JOIN livres l ON e.id_livre = l.id "
                + "JOIN abonnes a ON e.id_abonne = a.id "
                + "WHERE p.id = ?";
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

    private Penalite mapRow(ResultSet rs) throws SQLException {
        Penalite p = new Penalite();
        p.setId(rs.getInt("id"));
        p.setIdEmprunt(rs.getInt("id_emprunt"));
        p.setMontant(rs.getDouble("montant"));
        Date dc = rs.getDate("date_calcul");
        p.setDateCalcul(dc != null ? dc.toLocalDate() : null);
        p.setStatutPaiement(rs.getString("statut_paiement"));
        p.setTitreLivre(rs.getString("titre_livre"));
        p.setNomAbonne(rs.getString("nom_abonne"));
        p.setIdAbonne(rs.getInt("id_abonne"));
        return p;
    }
}
