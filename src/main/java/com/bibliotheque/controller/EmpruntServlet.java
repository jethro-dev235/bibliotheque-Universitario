package com.bibliotheque.controller;

import com.bibliotheque.dao.AbonneDAO;
import com.bibliotheque.dao.EmpruntDAO;
import com.bibliotheque.dao.LivreDAO;
import com.bibliotheque.model.Abonne;
import com.bibliotheque.model.Livre;
import com.bibliotheque.util.FlashMessage;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Servlet de gestion des emprunts et retours.
 */
public class EmpruntServlet extends HttpServlet {

    private final EmpruntDAO empruntDAO = new EmpruntDAO();
    private final LivreDAO livreDAO = new LivreDAO();
    private final AbonneDAO abonneDAO = new AbonneDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = defaultAction(request.getParameter("action"));
        try {
            switch (action) {
                case "new":
                    showForm(request, response);
                    break;
                case "retour":
                    int id = parseId(request);
                    empruntDAO.findById(id).ifPresent(e -> request.setAttribute("emprunt", e));
                    request.getRequestDispatcher("/WEB-INF/views/emprunts/retour.jsp").forward(request, response);
                    break;
                case "retards":
                    request.setAttribute("emprunts", empruntDAO.findEnRetard());
                    FlashMessage.transferToRequest(request);
                    request.getRequestDispatcher("/WEB-INF/views/emprunts/retards.jsp").forward(request, response);
                    break;
                default:
                    request.setAttribute("emprunts", empruntDAO.findEnCours());
                    FlashMessage.transferToRequest(request);
                    request.getRequestDispatcher("/WEB-INF/views/emprunts/list.jsp").forward(request, response);
                    break;
            }
        } catch (SQLException e) {
            throw new ServletException(e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = defaultAction(request.getParameter("action"));
        try {
            if ("create".equals(action)) {
                List<String> errors = validateCreate(request);
                if (!errors.isEmpty()) {
                    request.setAttribute("errors", errors);
                    showForm(request, response);
                    return;
                }
                int idLivre = Integer.parseInt(request.getParameter("idLivre"));
                int idAbonne = Integer.parseInt(request.getParameter("idAbonne"));
                Optional<Abonne> abonne = abonneDAO.findById(idAbonne);
                if (abonne.isEmpty() || !"ACTIF".equals(abonne.get().getStatut())) {
                    FlashMessage.setError(request, "Abonne inactif ou introuvable.");
                    response.sendRedirect(request.getContextPath() + "/emprunts?action=new");
                    return;
                }
                if (!livreDAO.decrementDisponible(idLivre)) {
                    FlashMessage.setError(request, "Aucun exemplaire disponible.");
                    response.sendRedirect(request.getContextPath() + "/emprunts?action=new");
                    return;
                }
                LocalDate debut = LocalDate.parse(request.getParameter("dateEmprunt"));
                LocalDate fin = LocalDate.parse(request.getParameter("dateRetourPrevue"));
                empruntDAO.insert(idLivre, idAbonne, debut, fin);
                FlashMessage.setSuccess(request, "Emprunt enregistre.");
                response.sendRedirect(request.getContextPath() + "/emprunts");
            } else if ("retour".equals(action)) {
                int id = parseId(request);
                Optional<com.bibliotheque.model.Emprunt> opt = empruntDAO.findById(id);
                if (opt.isEmpty()) {
                    FlashMessage.setError(request, "Emprunt introuvable.");
                    response.sendRedirect(request.getContextPath() + "/emprunts");
                    return;
                }
                LocalDate dateRetour = LocalDate.parse(request.getParameter("dateRetour"));
                empruntDAO.enregistrerRetour(id, dateRetour);
                livreDAO.incrementDisponible(opt.get().getIdLivre());
                FlashMessage.setSuccess(request, "Retour enregistre.");
                response.sendRedirect(request.getContextPath() + "/emprunts");
            }
        } catch (SQLException e) {
            FlashMessage.setError(request, "Erreur : " + e.getMessage());
            response.sendRedirect(request.getContextPath() + "/emprunts");
        }
    }

    private void showForm(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, ServletException, IOException {
        List<Livre> livres = livreDAO.findAll().stream().filter(l -> l.getNbDisponibles() > 0).collect(java.util.stream.Collectors.toList());
        request.setAttribute("livres", livres);
        request.setAttribute("abonnes", abonneDAO.findActifs());
        request.setAttribute("dateEmprunt", LocalDate.now().toString());
        request.setAttribute("dateRetourPrevue", LocalDate.now().plusDays(14).toString());
        FlashMessage.transferToRequest(request);
        request.getRequestDispatcher("/WEB-INF/views/emprunts/form.jsp").forward(request, response);
    }

    private List<String> validateCreate(HttpServletRequest request) {
        List<String> errors = new ArrayList<>();
        try {
            Integer.parseInt(request.getParameter("idLivre"));
            Integer.parseInt(request.getParameter("idAbonne"));
            LocalDate.parse(request.getParameter("dateEmprunt"));
            LocalDate fin = LocalDate.parse(request.getParameter("dateRetourPrevue"));
            if (fin.isBefore(LocalDate.parse(request.getParameter("dateEmprunt")))) {
                errors.add("La date de retour doit etre apres la date d'emprunt.");
            }
        } catch (Exception e) {
            errors.add("Veuillez remplir tous les champs correctement.");
        }
        return errors;
    }

    private int parseId(HttpServletRequest request) {
        return Integer.parseInt(request.getParameter("id"));
    }

    private String defaultAction(String action) {
        return action == null ? "list" : action;
    }
}
