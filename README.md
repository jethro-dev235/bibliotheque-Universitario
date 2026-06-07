# Bibliotheque Universitaire

Application web Java EE (MVC) de gestion de bibliotheque universitaire avec base de données H2 embarquée.

## 📋 Description

Application complète de gestion de bibliothèque universitaire permettant de :
- Gérer les livres (ajout, modification, suppression)
- Gérer les abonnés (inscription, suspension, réactivation)
- Gérer les emprunts et retours
- Suivre les emprunts en retard
- Calculer et gérer les pénalités

## 🛠 Technologies

- **Java 11**
- **Java EE (Jakarta EE 9)**
- **Maven 3.8+**
- **Tomcat 10** (via cargo-maven-plugin)
- **H2 Database** (base de données embarquée)
- **JSP/JSTL**
- **Bootstrap 5** pour l'interface

## 🚀 Installation et Lancement

### Prérequis

- JDK 11 installé
- Maven 3.8+ installé

### Méthode 1 : Script Windows (Recommandé)

```bash
cd bibliotheque-universitaire
run.bat
```

### Méthode 2 : Commandes Maven

```bash
cd bibliotheque-universitaire
mvn clean package cargo:run
```

L'application sera accessible à : **http://localhost:8080/bibliotheque**

## 🔐 Identifiants de connexion

- **Email :** admin@biblio.com
- **Mot de passe :** admin

## 📁 Structure du projet

```
bibliotheque-universitaire/
├── src/main/
│   ├── java/com/bibliotheque/
│   │   ├── controller/      # Servlets (Auth, Dashboard, Livres, Abonnés, Emprunts)
│   │   ├── dao/             # Accès aux données
│   │   ├── model/           # Modèles métier
│   │   ├── filter/          # Filtres d'authentification
│   │   └── util/            # Utilitaires (FlashMessage, PasswordUtil)
│   ├── resources/
│   │   ├── database.sql     # Script d'initialisation de la BDD
│   │   └── db.properties    # Configuration H2
│   └── webapp/
│       ├── WEB-INF/views/   # Pages JSP
│       └── assets/          # CSS, JS
├── pom.xml                  # Configuration Maven
└── run.bat                  # Script de lancement Windows
```

## 💾 Base de données

La base de données H2 est créée automatiquement au premier démarrage dans le fichier `bibliotheque_universitaire.mv.db`. Aucune installation de base de données externe n'est requise.

## 🎯 Fonctionnalités

### Gestion des livres
- Liste des livres avec disponibilité
- Ajout de nouveaux livres
- Modification des informations
- Suppression de livres

### Gestion des abonnés
- Liste des abonnés avec statut
- Inscription de nouveaux abonnés
- Suspension/réactivation d'abonnés
- Consultation de l'historique des emprunts

### Gestion des emprunts
- Création d'emprunts
- Enregistrement des retours
- Liste des emprunts en cours
- Liste des emprunts en retard

### Pénalités
- Calcul automatique des pénalités pour les retards
- Suivi des paiements

## 📝 Auteur

[Jethro-dev235](https://github.com/jethro-dev235)

## 📄 Licence

Ce projet est fourni à des fins éducatives.
