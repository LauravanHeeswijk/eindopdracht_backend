# Installatiehandleiding – Ryan Holiday | Boeken API

## Inhoudsopgave
1. Inleiding
2. Benodigdheden
3. Projectstructuur
4. Gebruikte technieken & frameworks
5. Installatie & configuratie
6. Project lokaal draaien
7. Testen
8. Endpoints (mini-overzicht)
9. Gebruikers & autorisatie

---

## 1. Inleiding
**Ryan Holiday – Boeken API** is een Spring Boot backend waarmee je boeken kunt **bekijken**, **downloaden/lezen** en aan je **eigen bibliotheek** kunt toevoegen.  
Publieke endpoints zijn lezen/bekijken; **mutaties** zijn beveiligd met **JWT**.

---

## 2. Benodigdheden
- **Java 21**
- **Maven 3.8+**
- **Spring Boot 3.5.x**
- **Git** (repo klonen)
- **Postman** of cURL (testen)
- **IDE** (IntelliJ/VS Code) – optioneel

---

## 3. Projectstructuur

```
Backend_eindopdracht/
├─ file_uploads/                 # (DEV) uploadmap die de app gebruikt
├─ src/
│  ├─ main/java/nl/laura/boekenapi/
│  │  ├─ config/ controller/ dto/ exception/ filter/ mapper/ model/ repository/ service/ utils/
│  ├─ main/resources/            # application.properties, data.sql
│  └─ test/java/...              # unit- & integratietests
├─ pom.xml                       # Maven configuratie
├─ mvnw  mvnw.cmd
└─ .mvn/

```

> De API draait standaard op **poort 8081**.

---

## 4. Gebruikte technieken & frameworks
- **Spring Boot** (Web, Security/JWT, Validation)
- **Spring Data JPA (Hibernate)**
- **H2** (in-memory dev/test)
- **Bean Validation (Jakarta)**
- **JUnit 5 / Mockito / Spring Test (MockMvc)**
- Eenvoudige **FileStorageService** voor lokale uploads

---

## 5. Installatie & configuratie

**Stap 1 Repositorie klonen**
```bash
git clone: https://github.com/LauravanHeeswijk/eindopdracht_backend.git
cd eindopdracht_backend
```

**Stap 2 Applicatie-instellingen**

Controlleer of de config er zo uit ziet in: **`src/main/resources/application.properties`**;

```properties
# App 
spring.datasource.url=${DB_URL:jdbc:postgresql://localhost:5432/boeken_db}
spring.datasource.username=${DB_USERNAME:boeken_user}
spring.datasource.password=${DB_PASSWORD:change-me}
spring.jpa.hibernate.ddl-auto=update
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.PostgreSQLDialect

spring.sql.init.mode=always
spring.jpa.defer-datasource-initialization=true

jwt.secret=${JWT_SECRET:CHANGE_ME_IN_PROD}
jwt.expiration-ms=864000000

file.upload-dir=${FILE_UPLOAD_DIR:file_uploads}
spring.servlet.multipart.max-file-size=25MB
spring.servlet.multipart.max-request-size=25MB

spring.jpa.open-in-view=false
logging.level.org.springframework=INFO


```
## 6. Project lokaal draaien
```
cd fullstack-eindopdracht-backend
mvn clean install
mvn spring-boot:run
```

De API draait standaard op:
👉 http://localhost:8081





