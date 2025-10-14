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

## Inleiding
De **Boeken API** is een Java Spring Boot REST-backend waarmee gebruikers boeken van **Ryan Holiday** kunnen bekijken, downloaden en beheren.

Rollen:
- **USER** – boeken bekijken, aan eigen bibliotheek toevoegen, downloaden
- **ADMIN** – beheren van boeken, auteurs, categorieën en bestanden

Belangrijk:
- JWT-authenticatie (inloggen/registreren)
- CRUD voor boeken (admin)
- Bestandsupload & download
- “Mijn boeken”-bibliotheek
- Downloadgeschiedenis
- Rolgebaseerde toegang

---

## Benodigdheden
| Component     | Versie / Opmerking                            |
|---------------|-----------------------------------------------|
| Java          | 21                                            |
| Spring Boot   | 3.5.5                                         |
| Maven         | 3.8+                                          |
| PostgreSQL    | 14+ (runtime)                                 |
| H2            | Alleen voor **tests** (in-memory database)    |
| IntelliJ IDEA | Aanbevolen                                    |
| Postman       | Voor API-calls                                |
| Git           | Voor versiebeheer                             |

---

### Projectstructuur

De backend heeft een duidelijke **3-lagen architectuur** (Controller | Service | Repository), conform de best practices van Spring Boot.

```plaintext
eindopdracht_backend_final/
├── .idea/
├── .mvn/
├── build/
├── file_uploads/
├── src/
│   └── main/
│       ├── java/
│       │   └── nl/
│       │       └── laura/
│       │           └── boekenapi/
│       │               ├── config/
│       │               ├── controller/
│       │               ├── dto/
│       │               ├── exception/
│       │               ├── filter/
│       │               ├── mapper/
│       │               ├── model/
│       │               ├── repository/
│       │               ├── service/
│       │               ├── utils/
│       │               ├── BackendEindopdrachtApplication.java
│       │               └── GenerateBCrypt.java
│       └── resources/
│           ├── application.properties
│           └── data.sql
├── test/
├── target/
├── uploads/
├── .gitattributes
├── .gitignore
├── mvnw
├── mvnw.cmd
├── pom.xml
└── README.md

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
- **Postman** 

---

## 5. Installatie en configuratie

###  Stap 1: Repositories klonen
```bash
 
 git clone https://github.com/LauravanHeeswijk/eindopdracht_backend
 cd eindopdracht_backend
```

###  Stap 2: Backend configureren

In src/main/resources/application.properties staan de basisinstellingen.
Voorbeeld:


PostgreSQL

```
spring.datasource.url=jdbc:postgresql://localhost:5432/boekenapi
spring.datasource.username=postgres
spring.datasource.password=Password
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

Data initialiseren uit data.sql
```
Gebruikersnaam: "laura@example.com"
Wachtwoord: "secret"
```

## 6. Project lokaal draaien

```
Open http://localhost:8082/api/health | moet OK teruggeven.

```

```
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
 :: Spring Boot ::               (v3.5.5)
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

## 8. Gebruikers en autorisatie
| Rol    | E-mail               | Wachtwoord | Rechten                                      |
|--------|----------------------|------------|----------------------------------------------|
| ADMIN  | admin@example.com    | secret     | CRUD op boeken/auteurs/categorieën, upload   |
| USER   | laura@example.com    | secret     | Boeken bekijken, bibliotheek, downloaden     |
| Test user | testuser@example.com | secret | USER – boeken bekijken, bibliotheek, downloaden |
                                       |
