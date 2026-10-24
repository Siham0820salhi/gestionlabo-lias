# 🔬 GestionLabo LIAS — Application Web de Gestion de Laboratoire

<div align="center">

![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.4.2-brightgreen?style=for-the-badge&logo=springboot)
![Java](https://img.shields.io/badge/Java-21%20LTS-orange?style=for-the-badge&logo=java)
![MySQL](https://img.shields.io/badge/MySQL-8.0-blue?style=for-the-badge&logo=mysql)
![Docker](https://img.shields.io/badge/Docker-Compose-2496ED?style=for-the-badge&logo=docker)
![Thymeleaf](https://img.shields.io/badge/Thymeleaf-3.4.2-005F0F?style=for-the-badge&logo=thymeleaf)
![Bootstrap](https://img.shields.io/badge/Bootstrap-5.3-7952B3?style=for-the-badge&logo=bootstrap)

**Application web complète de gestion administrative et scientifique du Laboratoire LIAS**  
Faculté des Sciences Ben M'Sik — Université Hassan II de Casablanca

[🚀 Démarrage rapide](#-démarrage-rapide) • [📦 Structure](#-structure-du-projet) • [🔐 Comptes](#-comptes-par-défaut) • [📸 Aperçu](#-aperçu)

</div>

---

## 📋 Description

**GestionLabo** est une plateforme web développée dans le cadre du **Master Data Science & Big Data 2025-2026** (Projet JEE). Elle centralise l'ensemble des activités administratives et scientifiques du laboratoire **LIAS** (Laboratoire d'Intelligence Artificielle et Systèmes) dans une interface moderne, sécurisée et responsive.

### 🎯 Objectifs
- Gérer le cycle de vie complet des membres (permanent, doctorant, associé, retraité, ancien)
- Suivre les productions scientifiques (publications, événements, conventions)
- Administrer le matériel avec historique complet des distributions
- Faciliter la communication interne (messagerie, notifications)
- Générer automatiquement les rapports annuels d'activité en PDF
- Contrôler les accès par rôles (Directeur, Admin, Permanent, Doctorant, Associé)

---

## 🛠️ Stack Technologique

| Technologie | Version | Rôle |
|---|---|---|
| **Spring Boot** | 3.4.2 | Framework principal + Tomcat embarqué |
| **Spring Security** | 3.4.2 | Authentification & autorisation par rôles |
| **Spring Data JPA** | 3.4.2 | ORM — accès base de données |
| **Hibernate** | 6.6.5 | Implémentation JPA — génération SQL |
| **Thymeleaf** | 3.4.2 | Moteur de templates HTML côté serveur |
| **MySQL** | 8.0 | Base de données relationnelle |
| **Docker Compose** | - | Conteneurisation MySQL |
| **OpenPDF** | 1.3.30 | Génération de rapports PDF |
| **Spring Mail** | 3.4.2 | Envoi d'emails via Gmail SMTP |
| **Bootstrap** | 5.3.0 | Interface responsive |
| **FullCalendar** | - | Vue calendrier interactive |
| **Font Awesome** | 6.4.0 | Icônes vectorielles |
| **Java** | 21 LTS | Langage de programmation |
| **Maven** | 3.x | Gestion des dépendances |

---

## 📦 Structure du Projet

```
gestionlabo/
│
├── 📄 pom.xml                          # Dépendances Maven (12 dépendances)
├── 📄 docker-compose.yml               # Configuration MySQL en Docker
├── 📄 HELP.md                          # Aide Spring Boot
├── 📄 mvnw / mvnw.cmd                  # Maven Wrapper (Linux/Windows)
│
├── 📁 src/
│   ├── 📁 main/
│   │   ├── 📁 java/com/lias/gestionlabo/
│   │   │   │
│   │   │   ├── 🚀 GestionlaboApplication.java     # Point d'entrée + seed Admin & Directeur
│   │   │   │
│   │   │   ├── 📁 config/
│   │   │   │   ├── SecurityConfig.java            # Règles Spring Security par URL et rôle
│   │   │   │   ├── WebMvcConfig.java              # Handlers ressources statiques (uploads)
│   │   │   │   └── GlobalControllerAdvice.java    # Injection isDirecteurActif dans tous les modèles
│   │   │   │
│   │   │   ├── 📁 models/                         # 18 Entités JPA (@Entity)
│   │   │   │   ├── Membre.java                    # Profil complet d'un membre
│   │   │   │   ├── Publication.java               # Articles, thèses, brevets
│   │   │   │   ├── Evenement.java                 # Conférences, séminaires, workshops
│   │   │   │   ├── Document.java                  # Fichiers liés aux événements
│   │   │   │   ├── Materiel.java                  # Inventaire des équipements
│   │   │   │   ├── DemandeMateriel.java           # Demandes d'emprunt de matériel
│   │   │   │   ├── DistributionMateriel.java      # Historique des distributions
│   │   │   │   ├── DemandeAdhesion.java           # Candidatures d'adhésion au labo
│   │   │   │   ├── Mandat.java                    # Mandats de direction avec dates
│   │   │   │   ├── Message.java                   # Messagerie interne
│   │   │   │   ├── Notification.java              # Alertes individuelles
│   │   │   │   ├── Convention.java                # Accords et partenariats signés
│   │   │   │   ├── Partenaire.java                # Organismes partenaires
│   │   │   │   ├── Reunion.java                   # Réunions et procès-verbaux
│   │   │   │   ├── RapportAnnuel.java             # Synthèse statistique annuelle
│   │   │   │   ├── Laboratoire.java               # Informations du laboratoire
│   │   │   │   ├── HistoriqueAffiliation.java     # Historique des statuts membres
│   │   │   │   └── HistoriqueRoleEquipe.java      # Historique des rôles d'équipe
│   │   │   │
│   │   │   ├── 📁 models/enums/                   # 6 Énumérations
│   │   │   │   ├── StatutMembre.java              # PERMANENT | ASSOCIE | DOCTORANT | RETRAITE | ANCIEN
│   │   │   │   ├── RoleMembre.java                # DIRECTEUR | VICE_DIRECTEUR | CHEF_EQUIPE | MEMBRE
│   │   │   │   ├── StatutDemande.java             # EN_ATTENTE | ACCEPTEE | REFUSEE
│   │   │   │   ├── TypePret.java                  # DEFINITIF | TEMPORAIRE
│   │   │   │   ├── TypeEvenement.java             # CONFERENCE | SEMINAIRE | WORKSHOP
│   │   │   │   └── TypeDocument.java              # PROGRAMME | RAPPORT | ATTESTATION | ...
│   │   │   │
│   │   │   ├── 📁 repositories/                   # 18 Interfaces JpaRepository
│   │   │   │   ├── MembreRepository.java
│   │   │   │   ├── PublicationRepository.java
│   │   │   │   ├── EvenementRepository.java
│   │   │   │   ├── MaterielRepository.java
│   │   │   │   ├── DemandeMaterielRepository.java
│   │   │   │   ├── DistributionMaterielRepository.java
│   │   │   │   ├── DemandeAdhesionRepository.java
│   │   │   │   ├── MandatRepository.java
│   │   │   │   ├── MessageRepository.java
│   │   │   │   ├── NotificationRepository.java
│   │   │   │   ├── ConventionRepository.java
│   │   │   │   ├── PartenaireRepository.java
│   │   │   │   ├── ReunionRepository.java
│   │   │   │   ├── RapportAnnuelRepository.java
│   │   │   │   ├── DocumentRepository.java
│   │   │   │   ├── LaboratoireRepository.java
│   │   │   │   ├── HistoriqueAffiliationRepository.java
│   │   │   │   └── HistoriqueRoleEquipeRepository.java
│   │   │   │
│   │   │   ├── 📁 services/                       # 15 Classes @Service (logique métier)
│   │   │   │   ├── MembreService.java             # Gestion membres + statuts
│   │   │   │   ├── PublicationService.java        # Gestion publications
│   │   │   │   ├── EvenementService.java          # Gestion événements
│   │   │   │   ├── MaterielService.java           # Matériel + génération rapport PDF
│   │   │   │   ├── DemandeAdhesionService.java    # Traitement candidatures
│   │   │   │   ├── MandatService.java             # Gestion mandats de direction
│   │   │   │   ├── MessageService.java            # Messagerie interne
│   │   │   │   ├── NotificationService.java       # Système de notifications
│   │   │   │   ├── ConventionService.java         # Conventions et partenaires
│   │   │   │   ├── ReunionService.java            # Réunions et PV
│   │   │   │   ├── DocumentService.java           # Gestion documents uploadés
│   │   │   │   ├── RapportAnnuelService.java      # Génération rapport annuel
│   │   │   │   ├── RechercheService.java          # Recherche globale transversale
│   │   │   │   ├── RoleEquipeService.java         # Historique des rôles d'équipe
│   │   │   │   └── CustomUserDetailsService.java  # Chargement utilisateur pour Spring Security
│   │   │   │
│   │   │   └── 📁 controllers/                    # 14 Classes @Controller (60+ endpoints)
│   │   │       ├── LoginController.java           # Auth + page publique + demandes adhésion
│   │   │       ├── MembreController.java          # CRUD membres + API JSON
│   │   │       ├── PublicationController.java     # Publications + upload PDF
│   │   │       ├── EvenementController.java       # Événements + API calendrier JSON
│   │   │       ├── MaterielController.java        # Matériel + distributions + rapport
│   │   │       ├── ProfileController.java         # Profil + upload photo
│   │   │       ├── MessageController.java         # Messagerie générale/équipe/événement
│   │   │       ├── NotificationController.java    # Notifications + marquage lu
│   │   │       ├── ConventionController.java      # Conventions et partenaires
│   │   │       ├── ReunionController.java         # Réunions et procès-verbaux
│   │   │       ├── MandatController.java          # Mandats de direction
│   │   │       ├── DocumentController.java        # Upload/téléchargement documents
│   │   │       ├── RechercheController.java       # Recherche globale
│   │   │       └── RoleEquipeController.java      # Assignation et historique des rôles
│   │   │
│   │   └── 📁 resources/
│   │       ├── 📄 application.properties          # Configuration DB, mail, upload, logs
│   │       ├── 📁 static/                         # CSS, JS, images statiques
│   │       └── 📁 templates/                      # 30 Templates Thymeleaf HTML
│   │           ├── sidebar.html                   # Fragment navigation (inclus partout)
│   │           ├── index.html                     # Page d'accueil publique
│   │           ├── login.html                     # Formulaire de connexion
│   │           ├── dashboard.html                 # Tableau de bord principal
│   │           ├── membres.html                   # Liste des membres
│   │           ├── form-membre.html               # Formulaire ajout/modification membre
│   │           ├── mon-profil.html                # Profil de l'utilisateur connecté
│   │           ├── publications.html              # Liste des publications
│   │           ├── form-publication.html          # Formulaire ajout publication + PDF
│   │           ├── public-publications.html       # Vue publique publications
│   │           ├── public-equipes.html            # Vue publique équipes
│   │           ├── public-activites.html          # Vue publique activités
│   │           ├── evenements.html                # Liste des événements
│   │           ├── form-evenement.html            # Formulaire création événement
│   │           ├── calendrier.html                # Vue calendrier FullCalendar
│   │           ├── messages.html                  # Interface messagerie
│   │           ├── notifications.html             # Liste des notifications
│   │           ├── documents.html                 # Gestionnaire de documents
│   │           ├── conventions.html               # Conventions et partenaires
│   │           ├── reunions.html                  # Réunions et procès-verbaux
│   │           ├── demandes.html                  # Demandes d'adhésion (Directeur)
│   │           ├── demander-adhesion.html         # Formulaire candidature (public)
│   │           ├── mes-demandes.html              # Mes demandes de matériel
│   │           ├── demande-materiel.html          # Formulaire demande d'emprunt
│   │           ├── admin-materiel.html            # Gestion complète matériel
│   │           ├── mandats.html                   # Gestion des mandats
│   │           ├── historique-roles.html          # Historique des rôles d'équipe
│   │           ├── rapport.html                   # Page rapport annuel
│   │           └── recherche.html                 # Résultats recherche globale
│   │
│   └── 📁 test/java/                              # Tests JUnit
│
├── 📁 uploads/                                    # Fichiers uploadés (généré automatiquement)
│   ├── 📁 publications/                           # PDFs des publications
│   ├── 📁 documents/                              # Documents des événements
│   ├── 📁 conventions/                            # Documents des conventions
│   └── 📁 reunions/                              # Procès-verbaux des réunions
│
└── 📁 target/                                     # Build Maven (généré automatiquement)
```

---

## 🗄️ Modèle de Données

L'application utilise **18 tables MySQL** créées automatiquement par Hibernate :

| Table | Description |
|---|---|
| `membres` | Profils complets des membres (central) |
| `demandes_adhesion` | Candidatures d'adhésion au laboratoire |
| `mandats` | Périodes de gouvernance (Directeur, Vice-Dir, Chef d'équipe) |
| `historique_affiliation` | Historique des statuts d'un membre |
| `historique_roles_equipes` | Historique des rôles dans les équipes |
| `publications` | Articles, thèses, brevets des chercheurs |
| `evenements` | Conférences, séminaires, workshops |
| `documents` | Fichiers liés aux événements |
| `materiel` | Inventaire des équipements du laboratoire |
| `demande_materiel` | Demandes d'emprunt soumises par les membres |
| `distribution_materiel` | Historique complet des distributions |
| `messages` | Messagerie interne (générale, équipe, événement) |
| `notifications` | Alertes individuelles pour chaque membre |
| `conventions` | Accords et partenariats signés |
| `partenaires` | Organismes partenaires du laboratoire |
| `reunions` | Réunions et procès-verbaux |
| `rapport_annuel` | Synthèses statistiques annuelles générées |
| `laboratoire` | Informations générales du laboratoire |

---

## 👥 Rôles et Permissions

| Rôle | Accès |
|---|---|
| **Visiteur** (sans compte) | Pages publiques : publications, équipes, activités, candidature |
| **DOCTORANT** | Publications, matériel (demande), messagerie, profil |
| **ASSOCIE** | Consultation, événements, messagerie — ❌ pas de matériel |
| **PERMANENT** | Tout DOCTORANT + membres, conventions, réunions |
| **DIRECTEUR** | Tout PERMANENT + adhésions, mandats, matériel (distribution), rapport PDF |
| **ADMIN** | Accès technique total — créé automatiquement au démarrage |

> ⚠️ **Règle importante** : Le statut (PERMANENT, DOCTORANT...) et le rôle (DIRECTEUR, CHEF_EQUIPE...) sont deux concepts séparés dans le système. Tout l'historique est conservé — aucune donnée n'est jamais supprimée.

---

## 🚀 Démarrage Rapide

### Prérequis
- Java 21 JDK
- Docker Desktop
- Maven 3.x
- Eclipse IDE for Enterprise Java (ou IntelliJ IDEA)

### Étape 1 — Démarrer MySQL avec Docker

```bash
docker-compose up -d --force-recreate
```

Vérifier que MySQL est healthy :
```bash
docker ps
# mysql-lias   Up X seconds (healthy)   0.0.0.0:3306->3306/tcp ✅
```

### Étape 2 — Lancer l'application

```bash
# Avec Maven Wrapper
./mvnw spring-boot:run

# Ou depuis Eclipse
# Clic droit sur GestionlaboApplication.java → Run As → Spring Boot App
```

### Étape 3 — Accéder à l'application

```
http://localhost:8080
```

---

## 🔐 Comptes par Défaut

Créés automatiquement au premier démarrage :

| Compte | Email | Mot de passe | Rôle |
|---|---|---|---|
| **Administrateur système** | `admin@lias.ma` | `Admin@2026` | ADMIN |
| **Directeur** | `siham.salhi2@lias.ma` | `secure123` | DIRECTEUR |

> 💡 Le compte ADMIN a `statutActuel = null` — il n'est pas un membre scientifique du labo, c'est un compte technique pur.

---

## 📸 Aperçu

| Page | Description |
|---|---|
| `/` | Page d'accueil publique avec candidature |
| `/login` | Formulaire de connexion sécurisé |
| `/dashboard` | Tableau de bord avec statistiques |
| `/membres` | Liste et gestion des membres |
| `/publications` | Publications scientifiques avec PDF |
| `/evenements/calendrier` | Calendrier interactif des événements |
| `/admin/materiel` | Gestion complète du matériel |
| `/admin/rapport` | Génération rapport annuel PDF |
| `/recherche` | Recherche globale transversale |

---

## ⚙️ Configuration

Le fichier `src/main/resources/application.properties` contient :

```properties
# Base de données MySQL
spring.datasource.url=jdbc:mysql://localhost:3306/lias_db
spring.datasource.username=root
spring.datasource.password=lias2026

# Hibernate — mise à jour automatique du schéma
spring.jpa.hibernate.ddl-auto=update

# Upload fichiers (max 5MB)
spring.servlet.multipart.max-file-size=5MB

# Email (Gmail SMTP)
spring.mail.host=smtp.gmail.com
spring.mail.port=587
```

---

## 📊 Statistiques du Projet

| Métrique | Valeur |
|---|---|
| Fichiers Java | 77+ |
| Entités JPA | 18 |
| Repositories | 18 |
| Services | 15 |
| Controllers | 14 |
| Templates HTML | 30 |
| Endpoints HTTP | 60+ |
| Tables MySQL | 18 |
| Énumérations | 6 |
| Dépendances Maven | 12 |

---

## 🏗️ Architecture MVC

```
Navigateur HTTP
      ↓
Spring Security Filter (authentification + autorisation)
      ↓
DispatcherServlet
      ↓
@Controller (14 classes) — reçoit la requête HTTP
      ↓
@Service (15 classes) — applique les règles métier
      ↓
@Repository (18 interfaces) — accès base de données
      ↓
Hibernate ORM — traduit Java ↔ SQL
      ↓
MySQL 8.0 (Docker) — stockage des données
      ↑
Thymeleaf Template Engine — génère le HTML
      ↑
Navigateur — affiche la page
```

---

## 👤 Auteur

**Siham Salhi**  
Master Data Science & Big Data — 2025-2026  
Faculté des Sciences Ben M'Sik, Université Hassan II de Casablanca  
Laboratoire LIAS

---

## 📄 Licence

Projet académique — Master Data Science & Big Data  
Faculté des Sciences Ben M'Sik — Université Hassan II de Casablanca © 2026
