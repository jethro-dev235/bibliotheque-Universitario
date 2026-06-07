package com.bibliotheque.controller;

import com.bibliotheque.dao.LivreDAO;
import com.bibliotheque.model.Livre;
import com.bibliotheque.util.FlashMessage;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Servlet CRUD et recherche des livres.
 */
public class LivreServlet extends HttpServlet {

    private final LivreDAO livreDAO = new LivreDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = defaultAction(request.getParameter("action"));
        try {
            switch (action) {
                case "new":
                    showForm(request, response, new Livre());
                    break;
                case "edit":
                    int id = parseId(request);
                    Optional<Livre> opt = livreDAO.findById(id);
                    if (opt.isEmpty()) {
                        FlashMessage.setError(request, "Livre introuvable.");
                        response.sendRedirect(request.getContextPath() + "/livres");
                        return;
                    }
                    showForm(request, response, opt.get());
                    break;
                case "delete":
                    int id2 = parseId(request);
                    livreDAO.delete(id2);
                    FlashMessage.setSuccess(request, "Livre supprime.");
                    response.sendRedirect(request.getContextPath() + "/livres");
                    break;
                default:
                    listLivres(request, response);
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
        List<String> errors = validate(request);
        if (!errors.isEmpty()) {
            request.setAttribute("errors", errors);
            request.setAttribute("livre", buildFromRequest(request));
            request.getRequestDispatcher("/WEB-INF/views/livres/form.jsp").forward(request, response);
            return;
        }
        try {
            Livre livre = buildFromRequest(request);
            if ("create".equals(action)) {
                livreDAO.insert(livre);
                FlashMessage.setSuccess(request, "Livre ajoute avec succes.");
            } else if ("update".equals(action)) {
                livreDAO.update(livre);
                FlashMessage.setSuccess(request, "Livre mis a jour.");
            }
            response.sendRedirect(request.getContextPath() + "/livres");
        } catch (SQLException e) {
            FlashMessage.setError(request, "Erreur base de donnees : " + e.getMessage());
            response.sendRedirect(request.getContextPath() + "/livres");
        }
    }

    private void listLivres(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, ServletException, IOException {
        String titre = request.getParameter("titre");
        String auteur = request.getParameter("auteur");
        String isbn = request.getParameter("isbn");
        String categorie = request.getParameter("categorie");
        boolean search = hasValue(titre) || hasValue(auteur) || hasValue(isbn) || hasValue(categorie);
        List<Livre> livres = search
                ? livreDAO.search(titre, auteur, isbn, categorie)
                : livreDAO.findAll();
        request.setAttribute("livres", livres);
        request.setAttribute("titre", titre);
        request.setAttribute("auteur", auteur);
        request.setAttribute("isbn", isbn);
        request.setAttribute("categorie", categorie);
        FlashMessage.transferToRequest(request);
        request.getRequestDispatcher("/WEB-INF/views/livres/list.jsp").forward(request, response);
    }

    private void showForm(HttpServletRequest request, HttpServletResponse response, Livre livre)
            throws ServletException, IOException {
        request.setAttribute("livre", livre);
        FlashMessage.transferToRequest(request);
        request.getRequestDispatcher("/WEB-INF/views/livres/form.jsp").forward(request, response);
    }

    private List<String> validate(HttpServletRequest request) {
        List<String> errors = new ArrayList<>();
        if (isBlank(request.getParameter("titre"))) {
            errors.add("Le titre est obligatoire.");
        }
        if (isBlank(request.getParameter("auteur"))) {
            errors.add("L'auteur est obligatoire.");
        }
        if (isBlank(request.getParameter("isbn"))) {
            errors.add("L'ISBN est obligatoire.");
        }
        if (isBlank(request.getParameter("categorie"))) {
            errors.add("La categorie est obligatoire.");
        }
        try {
            int annee = Integer.parseInt(request.getParameter("anneePublication"));
            if (annee < 1000 || annee > 2100) {
                errors.add("Annee de publication invalide.");
            }
        } catch (NumberFormatException e) {
            errors.add("Annee de publication invalide.");
        }
        try {
            int nb = Integer.parseInt(request.getParameter("nbExemplaires"));
            if (nb < 1) {
                errors.add("Le nombre d'exemplaires doit etre >= 1.");
            }
        } catch (NumberFormatException e) {
            errors.add("Nombre d'exemplaires invalide.");
        }
        return errors;
    }

    private Livre buildFromRequest(HttpServletRequest request) {
        Livre l = new Livre();
        String idStr = request.getParameter("id");
        if (idStr != null && !idStr.isBlank()) {
            l.setId(Integer.parseInt(idStr));
        }
        l.setTitre(request.getParameter("titre").trim());
        l.setAuteur(request.getParameter("auteur").trim());
        l.setIsbn(request.getParameter("isbn").trim());
        l.setCategorie(request.getParameter("categorie").trim());
        int nbEx = Integer.parseInt(request.getParameter("nbExemplaires"));
        l.setAnneePublication(Integer.parseInt(request.getParameter("anneePublication")));
        l.setNbExemplaires(nbEx);
        String dispoStr = request.getParameter("nbDisponibles");
        if (dispoStr != null && !dispoStr.isBlank()) {
            l.setNbDisponibles(Integer.parseInt(dispoStr));
        } else {
            l.setNbDisponibles(nbEx);
        }
        if (l.getNbDisponibles() > l.getNbExemplaires()) {
            l.setNbDisponibles(l.getNbExemplaires());
        }
        return l;
    }

    private int parseId(HttpServletRequest request) {
        return Integer.parseInt(request.getParameter("id"));
    }

    private String defaultAction(String action) {
        return action == null ? "list" : action;
    }

    private boolean isBlank(String s) {
        return s == null || s.trim().isEmpty();
    }

    private boolean hasValue(String s) {
        return s != null && !s.trim().isEmpty();
    }
}
