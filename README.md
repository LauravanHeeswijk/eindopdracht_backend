# Installatiehandleiding Ryan Holiday | Boeken API 


## Inhoudsopgave
1. [Inleiding](#1-inleiding)
2. [Benodigdheden](#2-benodigdheden)
3. [Projectstructuur](#3-projectstructuur)
4. [Gebruikte technieken & frameworks](#4-gebruikte-technieken--frameworks)
5. [Installatie en configuratie](#5-installatie-en-configuratie)
6. [Project lokaal draaien](#6-project-lokaal-draaien)
7. [Testen](#7-testen)
8. [Gebruikers en autorisatie](#8-gebruikers-en-autorisatie)

---

## 1. Inleiding

De **BoekenAPI** is een Java Spring Boot REST-backend waarmee gebruikers digitale boeken van **Ryan Holiday** kunnen bekijken, downloaden en toevoegen aan hun persoonlijke bibliotheek.

De API ondersteunt **twee rollen**:
-  **USER** – boeken bekijken, toevoegen aan bibliotheek, downloaden
-  **ADMIN** – boeken, auteurs, categorieën en bestanden beheren

### Belangrijkste functionaliteiten
- Gebruikersregistratie en inloggen (JWT-authenticatie)
- CRUD-functionaliteit voor boeken (beheerder)
- Bestandsupload/download met logging
- Gebruikersbibliotheek (“mijn boeken”)
- Downloadgeschiedenis per gebruiker
- Rolgebaseerde toegang (User vs Admin)



## 2. Benodigdheden

Om de applicatie lokaal te draaien heb je nodig:

| Component | Versie / Opmerking                |
|---------|-----------------------------------|
| Java | 21                                |
| Spring Boot | 3.3+                              |
| Maven | 3.8+                              |
| PostgreSQL | 14+ (of H2 in-memory voor testen) |
|  IDE | IntelliJ IDEA                     |
|  Postman | Voor API tests                    |
|  Git | Voor versiebeheer                 |

---

### Projectstructuur

De backend heeft een duidelijke **3-lagen architectuur** (Controller | Service | Repository), conform de best practices van Spring Boot.

```plaintext
fullstack-eindopdracht-backend/
│
├── src/
│   └── main/
│       ├── java/com/example/boekenapi/
│       │   ├── controller/          # REST endpoints (API-routes)
│       │   ├── service/             # Business logica
│       │   ├── repository/          # Database interactie (JPA)
│       │   ├── model/               # JPA-entiteiten (User, Book, Category, etc.)
│       │   ├── dto/                 # Data Transfer Objects (requests/responses)
│       │   ├── security/            # JWT-configuratie, filters en auth-logic
│       │   └── exception/           # Globale foutafhandeling (ControllerAdvice)
│       │
│       └── resources/
│           ├── application.properties    # Database- en serverconfiguratie
│           ├── data.sql                  # Seed data (users, boeken, categorieën)
│           └── static/                   # Eventuele statische bestanden
│
├── file_uploads/                         # Upload-map voor PDF-bestanden
│
├── pom.xml                               # Maven dependencies en projectmetadata
│
└── README.md                             # Installatie- en gebruiksdocumentatie

```

## 4. Gebruikte technieken & frameworks

###  Backend
- **Spring Boot** – REST API framework
- **Spring Security** – JWT authenticatie en autorisatie
- **Spring Data JPA** – ORM voor database
- **Hibernate** – implementatie van JPA
- **Validation API (@Valid)** – inputvalidatie
- **H2 / PostgreSQL** – databases

###  Testing
- **JUnit 5**
- **Mockito**
- **Postman** collectie: _BoekenAPI Full Flow (User + Admin)_

---

## 5. Installatie en configuratie

###  Stap 1: Repositories klonen
```bash
# Backend
git clone https://github.com/LauravanHeeswijk/eindopdracht_backend
cd eindopdracht_backend
```

### Database installeren:

PostgreSQL

```
spring.datasource.url=jdbc:postgresql://localhost:5432/boekenapi
spring.datasource.username=postgres
spring.datasource.password=secret
spring.jpa.hibernate.ddl-auto=update
spring.sql.init.mode=always
spring.jpa.show-sql=true

# Bestandsupload configuratie
file.upload-dir=./file_uploads
spring.servlet.multipart.max-file-size=10MB
spring.servlet.multipart.max-request-size=10MB
```

H2
```
spring.datasource.url=jdbc:h2:mem:testdb
spring.datasource.driverClassName=org.h2.Driver
spring.datasource.username=sa
spring.datasource.password=
spring.jpa.hibernate.ddl-auto=create-drop
spring.h2.console.enabled=true
```

Data initialiseren
```
Gebruikersnaam: "test@example.com"
Wachtwoord: "secret"
```

## 6. Project lokaal draaien
```
cd eindopdracht_backend
mvn clean install
mvn spring-boot:run
```
Voorbeeld log:

```
 .   ____          _            __ _ _
 /\\ / ___'_ __ _ _(_)_ __  __ _ \ \ \ \
( ( )\___ | '_ | '_| | '_ \/ _` | \ \ \ \
 \\/  ___)| |_)| | | | | || (_| |  ) ) ) )
  '  |____| .__|_| |_|_| |_\__, | / / / /
 =========|_|==============|___/=/_/_/_/
 :: Spring Boot ::               (v3.x)
```

## 7. Testen

Spring Boot tests draaien met Maven:
```
mvn test
```

Voorbeeld output terminal:

```
-------------------------------------------------------
T E S T S
-------------------------------------------------------
Tests run: 8, Failures: 0, Errors: 0, Skipped: 0
BUILD SUCCESS
```

## 8. Gebruikers en autoriatie

| Rol       | E-mailadres                                   | Wachtwoord | Rechten                                |
|-----------|-----------------------------------------------| ---------- | -------------------------------------- |
| **Admin** | [admin@example.com](mailto:admin@example.com) | secret     | CRUD op boeken, auteurs, uploads       |
| **User**  | [laura@example.com](mailto:laura@example.com) | secret     | Boeken bekijken, toevoegen, downloaden |
| **Test**  | [test@example.com](mailto:laura@example.com)  | secret     | Boeken bekijken, toevoegen, downloaden |