# Bibliotheque Universitaire

Application web Java EE (MVC) de gestion de bibliotheque universitaire.

## Prerequis

- JDK 17
- Maven 3.8+
- MySQL 8 (utilisateur `root` / mot de passe `root` par defaut)

## Configuration

Modifier `src/main/resources/db.properties` si necessaire.

## Lancement

```bash
cd bibliotheque-universitaire
mvn clean package tomcat10:run
```

Application : http://localhost:8080/bibliotheque

**Connexion admin :** `admin@biblio.com` / `admin123`

La base `bibliotheque_universitaire` est creee et peuplee au premier demarrage.
