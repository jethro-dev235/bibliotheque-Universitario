package com.bibliotheque.controller;

import com.bibliotheque.dao.AbonneDAO;
import com.bibliotheque.dao.EmpruntDAO;
import com.bibliotheque.model.Abonne;
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
 * Servlet CRUD des abonnes avec suspension et historique.
 */
public class AbonneServlet extends HttpServlet {

    private final AbonneDAO abonneDAO = new AbonneDAO();
    private final EmpruntDAO empruntDAO = new EmpruntDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = defaultAction(request.getParameter("action"));
        try {
            switch (action) {
                case "new":
                    Abonne a = new Abonne();
                    a.setDateInscription(LocalDate.now());
                    a.setStatut("ACTIF");
                    showForm(request, response, a);
                    break;
                case "edit":
                    int id = parseId(request);
                    Optional<Abonne> opt = abonneDAO.findById(id);
                    if (opt.isEmpty()) {
                        FlashMessage.setError(request, "Abonne introuvable.");
                        response.sendRedirect(request.getContextPath() + "/abonnes");
                        return;
                    }
                    showForm(request, response, opt.get());
                    break;
                case "delete":
                    abonneDAO.delete(parseId(request));
                    FlashMessage.setSuccess(request, "Abonne supprime.");
                    response.sendRedirect(request.getContextPath() + "/abonnes");
                    break;
                case "suspendre":
                    abonneDAO.suspendre(parseId(request));
                    FlashMessage.setSuccess(request, "Abonne suspendu.");
                    response.sendRedirect(request.getContextPath() + "/abonnes");
                    break;
                case "reactiver":
                    abonneDAO.reactiver(parseId(request));
                    FlashMessage.setSuccess(request, "Abonne reactive.");
                    response.sendRedirect(request.getContextPath() + "/abonnes");
                    break;
                case "historique":
                    int id2 = parseId(request);
                    request.setAttribute("historique", empruntDAO.findByAbonne(id2));
                    abonneDAO.findById(id2).ifPresent(a2 -> request.setAttribute("abonne", a2));
                    FlashMessage.transferToRequest(request);
                    request.getRequestDispatcher("/WEB-INF/views/abonnes/historique.jsp").forward(request, response);
                    break;
                default:
                    request.setAttribute("abonnes", abonneDAO.findAll());
                    FlashMessage.transferToRequest(request);
                    request.getRequestDispatcher("/WEB-INF/views/abonnes/list.jsp").forward(request, response);
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
            request.setAttribute("abonne", buildFromRequest(request));
            request.getRequestDispatcher("/WEB-INF/views/abonnes/form.jsp").forward(request, response);
            return;
        }
        try {
            Abonne abonne = buildFromRequest(request);
            if ("create".equals(action)) {
                abonneDAO.insert(abonne);
                FlashMessage.setSuccess(request, "Abonne cree.");
            } else if ("update".equals(action)) {
                abonneDAO.update(abonne);
                FlashMessage.setSuccess(request, "Abonne mis a jour.");
            }
            response.sendRedirect(request.getContextPath() + "/abonnes");
        } catch (SQLException e) {
            FlashMessage.setError(request, "Erreur : " + e.getMessage());
            response.sendRedirect(request.getContextPath() + "/abonnes");
        }
    }

    private void showForm(HttpServletRequest request, HttpServletResponse response, Abonne abonne)
            throws ServletException, IOException {
        request.setAttribute("abonne", abonne);
        FlashMessage.transferToRequest(request);
        request.getRequestDispatcher("/WEB-INF/views/abonnes/form.jsp").forward(request, response);
    }

    private List<String> validate(HttpServletRequest request) {
        List<String> errors = new ArrayList<>();
        if (isBlank(request.getParameter("nom"))) {
            errors.add("Le nom est obligatoire.");
        }
        if (isBlank(request.getParameter("prenom"))) {
            errors.add("Le prenom est obligatoire.");
        }
        if (isBlank(request.getParameter("email"))) {
            errors.add("L'email est obligatoire.");
        }
        try {
            LocalDate.parse(request.getParameter("dateInscription"));
        } catch (Exception e) {
            errors.add("Date d'inscription invalide.");
        }
        return errors;
    }

    private Abonne buildFromRequest(HttpServletRequest request) {
        Abonne a = new Abonne();
        String idStr = request.getParameter("id");
        if (idStr != null && !idStr.isBlank()) {
            a.setId(Integer.parseInt(idStr));
        }
        a.setNom(request.getParameter("nom").trim());
        a.setPrenom(request.getParameter("prenom").trim());
        a.setEmail(request.getParameter("email").trim());
        a.setTelephone(request.getParameter("telephone"));
        a.setDateInscription(LocalDate.parse(request.getParameter("dateInscription")));
        String statut = request.getParameter("statut");
        a.setStatut(statut != null ? statut : "ACTIF");
        return a;
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
}
