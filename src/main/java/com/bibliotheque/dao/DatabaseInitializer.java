package com.bibliotheque.dao;

import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Initialise la base de donnees au demarrage de l'application.
 */
public class DatabaseInitializer implements ServletContextListener {

    private static final Logger LOGGER = Logger.getLogger(DatabaseInitializer.class.getName());

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        try {
            DBConnection.loadProperties();
            executeScript();
            LOGGER.info("Base de donnees initialisee avec succes");
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Echec initialisation BDD: " + e.getMessage(), e);
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        // rien a liberer
    }

    /**
     * Execute le script database.sql ligne par ligne / instruction par instruction.
     */
    private void executeScript() throws IOException, SQLException {
        try (InputStream in = getClass().getClassLoader().getResourceAsStream("database.sql")) {
            if (in == null) {
                throw new IOException("database.sql introuvable");
            }
            StringBuilder script = new StringBuilder();
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    String trimmed = line.trim();
                    if (trimmed.isEmpty() || trimmed.startsWith("--")) {
                        continue;
                    }
                    script.append(line).append('\n');
                }
            }
            List<String> statements = parseStatements(script.toString());
            try (Connection serverConn = DBConnection.getServerConnection();
                 Statement stmt = serverConn.createStatement()) {
                for (String sql : statements) {
                    stmt.execute(sql);
                }
            }
        }
    }

    /**
     * Parse SQL statements handling semicolons inside string literals.
     */
    private List<String> parseStatements(String script) {
        List<String> statements = new ArrayList<>();
        StringBuilder current = new StringBuilder();
        boolean inSingleQuote = false;
        boolean inDoubleQuote = false;

        for (int i = 0; i < script.length(); i++) {
            char c = script.charAt(i);

            if (c == '\'' && !inDoubleQuote) {
                inSingleQuote = !inSingleQuote;
                current.append(c);
            } else if (c == '"' && !inSingleQuote) {
                inDoubleQuote = !inDoubleQuote;
                current.append(c);
            } else if (c == ';' && !inSingleQuote && !inDoubleQuote) {
                String stmt = current.toString().trim();
                if (!stmt.isEmpty()) {
                    statements.add(stmt);
                }
                current = new StringBuilder();
            } else {
                current.append(c);
            }
        }

        String last = current.toString().trim();
        if (!last.isEmpty()) {
            statements.add(last);
        }

        return statements;
    }
}
