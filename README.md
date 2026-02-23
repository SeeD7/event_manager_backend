# Event Management Platform

## Description

Plateforme de gestion d’événements collaboratifs.
Les utilisateurs peuvent créer, publier et rejoindre des événements.

## Stack technique

### Backend

- Java 17
- Spring Boot
- Spring Security (JWT)
- JPA / Hibernate
- Kafka (optionnel)

### Frontend

- Angular (standalone)
- Signals & RxJS
- Reactive Forms

## Architecture

- Séparation Front / Back
- DTO / Mapper
- Service layer
- Store NgRx limité

## Sécurité

- Authentification JWT
- Rôles utilisateurs
- Guards Angular

## CI/CD

- GitHub Actions
- Build & tests automatisés

## Lancer le projet

### Backend

```bash
./mvnw spring-boot:run
