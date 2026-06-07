package com.bibliotheque.dao;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * Gestionnaire de connexion JDBC a H2 Database.
 * Lit la configuration depuis db.properties.
 */
public final class DBConnection {

    private static String url;
    private static String user;
    private static String password;
    private static String driver;
    private static boolean initialized;

    private DBConnection() {
    }

    /**
     * Charge les proprietes de connexion depuis le classpath.
     */
    public static synchronized void loadProperties() throws IOException {
        if (initialized) {
            return;
        }
        Properties props = new Properties();
        try (InputStream in = DBConnection.class.getClassLoader().getResourceAsStream("db.properties")) {
            if (in == null) {
                throw new IOException("Fichier db.properties introuvable");
            }
            props.load(in);
        }
        url = props.getProperty("db.url", "jdbc:h2:./bibliotheque_universitaire;MODE=MySQL;AUTO_SERVER=TRUE");
        user = props.getProperty("db.user", "sa");
        password = props.getProperty("db.password", "");
        driver = props.getProperty("db.driver", "org.h2.Driver");
        initialized = true;
    }

    /**
     * Ouvre une connexion a la base bibliotheque_universitaire.
     *
     * @return connexion JDBC
     * @throws SQLException en cas d'erreur
     */
    public static Connection getConnection() throws SQLException {
        if (!initialized) {
            try {
                loadProperties();
            } catch (IOException e) {
                throw new SQLException("Impossible de charger db.properties", e);
            }
        }
        try {
            Class.forName(driver);
        } catch (ClassNotFoundException e) {
            throw new SQLException("Driver H2 introuvable", e);
        }
        return DriverManager.getConnection(url, user, password);
    }

    /**
     * Ouvre une connexion au serveur H2 (pour creer la base).
     */
    public static Connection getServerConnection() throws SQLException {
        return getConnection();
    }
}
