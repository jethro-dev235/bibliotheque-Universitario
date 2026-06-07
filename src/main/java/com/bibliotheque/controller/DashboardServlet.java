package com.bibliotheque.controller;

import com.bibliotheque.dao.AbonneDAO;
import com.bibliotheque.dao.EmpruntDAO;
import com.bibliotheque.dao.LivreDAO;
import com.bibliotheque.util.FlashMessage;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.SQLException;

/**
 * Servlet du tableau de bord avec statistiques.
 */
public class DashboardServlet extends HttpServlet {

    private final LivreDAO livreDAO = new LivreDAO();
    private final AbonneDAO abonneDAO = new AbonneDAO();
    private final EmpruntDAO empruntDAO = new EmpruntDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            request.setAttribute("countLivres", livreDAO.count());
            request.setAttribute("countAbonnes", abonneDAO.countActifs());
            request.setAttribute("countEmprunts", empruntDAO.countEnCours());
            request.setAttribute("countRetards", empruntDAO.countRetards());
            FlashMessage.transferToRequest(request);
            request.getRequestDispatcher("/WEB-INF/views/dashboard.jsp").forward(request, response);
        } catch (SQLException e) {
            throw new ServletException(e);
        }
    }
}
