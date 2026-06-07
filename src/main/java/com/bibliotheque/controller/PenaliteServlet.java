package com.bibliotheque.controller;

import com.bibliotheque.dao.AbonneDAO;
import com.bibliotheque.dao.PenaliteDAO;
import com.bibliotheque.model.Abonne;
import com.bibliotheque.util.FlashMessage;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Servlet de gestion des penalites.
 */
public class PenaliteServlet extends HttpServlet {

    private final PenaliteDAO penaliteDAO = new PenaliteDAO();
    private final AbonneDAO abonneDAO = new AbonneDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            Map<Integer, Double> totaux = penaliteDAO.totalImpayeParAbonne();
            List<Abonne> abonnes = abonneDAO.findAll();
            List<AbonneTotal> resume = new ArrayList<>();
            for (Abonne a : abonnes) {
                double total = totaux.getOrDefault(a.getId(), 0.0);
                if (total > 0) {
                    resume.add(new AbonneTotal(a, total));
                }
            }
            request.setAttribute("penalites", penaliteDAO.findAll());
            request.setAttribute("totauxAbonnes", resume);
            FlashMessage.transferToRequest(request);
            request.getRequestDispatcher("/WEB-INF/views/penalites/list.jsp").forward(request, response);
        } catch (SQLException e) {
            throw new ServletException(e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            int id = Integer.parseInt(request.getParameter("id"));
            penaliteDAO.marquerPaye(id);
            FlashMessage.setSuccess(request, "Penalite marquee comme payee.");
            response.sendRedirect(request.getContextPath() + "/penalites");
        } catch (SQLException e) {
            FlashMessage.setError(request, "Erreur : " + e.getMessage());
            response.sendRedirect(request.getContextPath() + "/penalites");
        }
    }

    /**
     * DTO pour afficher le total impaye par abonne.
     */
    public static class AbonneTotal {
        private final Abonne abonne;
        private final double total;

        public AbonneTotal(Abonne abonne, double total) {
            this.abonne = abonne;
            this.total = total;
        }

        public Abonne getAbonne() {
            return abonne;
        }

        public double getTotal() {
            return total;
        }
    }
}
