package com.bibliotheque.util;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

/**
 * Gestion des messages flash en session HTTP.
 */
public final class FlashMessage {

    public static final String SUCCESS = "flashSuccess";
    public static final String ERROR = "flashError";

    private FlashMessage() {
    }

    /**
     * Ajoute un message de succes en session.
     */
    public static void setSuccess(HttpServletRequest request, String message) {
        request.getSession().setAttribute(SUCCESS, message);
    }

    /**
     * Ajoute un message d'erreur en session.
     */
    public static void setError(HttpServletRequest request, String message) {
        request.getSession().setAttribute(ERROR, message);
    }

    /**
     * Transfere les messages flash vers les attributs de requete puis les supprime.
     */
    public static void transferToRequest(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session == null) {
            return;
        }
        Object success = session.getAttribute(SUCCESS);
        if (success != null) {
            request.setAttribute(SUCCESS, success);
            session.removeAttribute(SUCCESS);
        }
        Object error = session.getAttribute(ERROR);
        if (error != null) {
            request.setAttribute(ERROR, error);
            session.removeAttribute(ERROR);
        }
    }
}
