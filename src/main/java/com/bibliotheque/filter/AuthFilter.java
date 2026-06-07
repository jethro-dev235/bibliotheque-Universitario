package com.bibliotheque.filter;

import com.bibliotheque.model.Utilisateur;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

/**
 * Filtre d'authentification : protege toutes les URLs sauf login et ressources publiques.
 */
public class AuthFilter implements Filter {

  private static final String SESSION_USER = "utilisateur";

  @Override
  public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
      throws IOException, ServletException {
    HttpServletRequest req = (HttpServletRequest) request;
    HttpServletResponse res = (HttpServletResponse) response;
    String path = req.getRequestURI().substring(req.getContextPath().length());

    if (isPublicPath(path)) {
      chain.doFilter(request, response);
      return;
    }
    HttpSession session = req.getSession(false);
    Utilisateur user = session != null ? (Utilisateur) session.getAttribute(SESSION_USER) : null;
    if (user == null) {
      res.sendRedirect(req.getContextPath() + "/auth?action=login");
      return;
    }
    chain.doFilter(request, response);
  }

  private boolean isPublicPath(String path) {
    if (path == null || path.isEmpty() || "/".equals(path)) {
      return true;
    }
    return path.startsWith("/auth")
        || path.startsWith("/assets/")
        || path.endsWith(".css")
        || path.endsWith(".js")
        || path.equals("/index.jsp");
  }
}
