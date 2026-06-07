package com.bibliotheque.util;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Utilitaire de hachage SHA-256 pour les mots de passe.
 */
public final class PasswordUtil {

    private PasswordUtil() {
    }

    /**
     * Calcule le hash SHA-256 hexadecimal d'une chaine.
     *
     * @param plain mot de passe en clair
     * @return hash hexadecimal
     */
    public static String hashSha256(String plain) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(plain.getBytes(StandardCharsets.UTF_8));
            StringBuilder hex = new StringBuilder();
            for (byte b : hash) {
                hex.append(String.format("%02x", b));
            }
            return hex.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("SHA-256 non disponible", e);
        }
    }
}
