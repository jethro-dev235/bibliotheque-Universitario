package com.bibliotheque.controller;

import com.bibliotheque.dao.UtilisateurDAO;
import com.bibliotheque.model.Utilisateur;
import com.bibliotheque.util.FlashMessage;
import com.bibliotheque.util.PasswordUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Optional;

/**
 * Servlet d'authentification : connexion et deconnexion.
 */
public class AuthServlet extends HttpServlet {

    private static final String SESSION_USER = "utilisateur";
    private final UtilisateurDAO utilisateurDAO = new UtilisateurDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");
        if ("logout".equals(action)) {
            HttpSession session = request.getSession(false);
            if (session != null) {
                session.invalidate();
            }
            response.sendRedirect(request.getContextPath() + "/auth?action=login");
            return;
        }
        HttpSession session = request.getSession(false);
        if (session != null && session.getAttribute(SESSION_USER) != null) {
            response.sendRedirect(request.getContextPath() + "/dashboard");
            return;
        }
        FlashMessage.transferToRequest(request);
        request.getRequestDispatcher("/WEB-INF/views/login.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String email = trim(request.getParameter("email"));
        String password = request.getParameter("password");
        if (email == null || email.isEmpty() || password == null || password.isEmpty()) {
            request.setAttribute("errorLogin", "Email et mot de passe obligatoires.");
            request.getRequestDispatcher("/WEB-INF/views/login.jsp").forward(request, response);
            return;
        }
        try {
            Optional<Utilisateur> opt = utilisateurDAO.findByEmail(email);
            String hash = PasswordUtil.hashSha256(password);
            if (opt.isPresent() && hash.equals(opt.get().getMotDePasseHash())) {
                HttpSession session = request.getSession(true);
                session.setAttribute(SESSION_USER, opt.get());
                FlashMessage.setSuccess(request, "Bienvenue, " + opt.get().getNomComplet() + " !");
                response.sendRedirect(request.getContextPath() + "/dashboard");
                return;
            }
            request.setAttribute("errorLogin", "Identifiants incorrects.");
            request.setAttribute("email", email);
            request.getRequestDispatcher("/WEB-INF/views/login.jsp").forward(request, response);
        } catch (SQLException e) {
            request.setAttribute("errorLogin", "Erreur de connexion a la base de donnees.");
            request.getRequestDispatcher("/WEB-INF/views/login.jsp").forward(request, response);
        }
    }

    private String trim(String s) {
        return s == null ? null : s.trim();
    }
}
