# HackHub

> Piattaforma di gestione hackathon sviluppata come progetto universitario per il corso di **Ingegneria del Software** — Università di Camerino (prof. Polini, Morichetta).

**Autori:** Lorenzo Fino, Mattia Farabollini, Tommaso Leonardi — Anno Accademico 2025/2026

---

## Indice / Table of Contents

- 🇮🇹 [Italiano](#-hackhub--italiano)
- 🇬🇧 [English](#-hackhub--english)

---

# 🇮🇹 HackHub — Italiano

## Indice

- [Descrizione del Progetto](#descrizione-del-progetto)
- [Architettura](#architettura)
- [Design Pattern Applicati](#design-pattern-applicati)
- [Iterazioni](#iterazioni)
- [Setup e Avvio](#setup-e-avvio)
- [API REST — Postman](#api-rest--postman)

---

## Descrizione del Progetto

**HackHub** è una piattaforma software per la gestione completa del ciclo di vita di un hackathon universitario. Il sistema supporta i principali attori coinvolti — organizzatori, partecipanti (iscritti a team), mentori e giudici — e copre tutte le fasi operative: dalla pubblicazione e iscrizione, alla sottomissione dei progetti, alla valutazione e proclamazione del vincitore.

Il progetto è stato sviluppato nell'ambito del corso di **Ingegneria del Software** presso l'**Università di Camerino**, seguendo il **Processo Unificato (UP)** strutturato in cinque iterazioni incrementali. Per ogni iterazione sono stati prodotti:

- Diagramma dei casi d'uso
- Diagramma delle classi di analisi e progetto
- Diagrammi di sequenza per i casi d'uso principali
- Implementazione Java corrispondente

---

## Architettura

Il sistema adotta un'architettura a strati con separazione netta delle responsabilità:
Presentation  →  Application  →  Domain  →  Infrastructure
| Layer | Responsabilità |
|---|---|
| **presentation** | Controller REST, DTO request/response, mapper, gestione eccezioni HTTP |
| **application** | Service layer, orchestrazione della logica di business, DTO command/result |
| **domain** | Entità di dominio, interfacce repository, eccezioni custom, pattern State/Observer |
| **infrastructure** | Implementazioni JPA, adapter esterni (Calendar, Payment, Email), sicurezza JWT |

### Stack Tecnologico

| Componente | Tecnologia |
|---|---|
| Linguaggio | Java 17 |
| Framework | Spring Boot 4.0 |
| Database | H2 (in-memory, create-drop) |
| Sicurezza | Spring Security + JWT |
| Build tool | Maven |
| UML | Visual Paradigm (`.vpp`) |

---

## Design Pattern Applicati

### State — Ciclo di Vita dell'Hackathon

Il ciclo di vita dell'hackathon è modellato con il pattern **State**. La classe `Hackathon` funge da Context e delega le operazioni allo stato corrente:
Iscrizione → InSvolgimento → InValutazione → Concluso
Ogni stato definisce quali operazioni sono permesse e lancia eccezioni appropriate per transizioni non valide. La transizione tra stati avviene automaticamente tramite `HackathonScheduler` (attore Tempo) o manualmente tramite l'organizzatore (proclamazione vincitore).

### Observer — Notifiche su Cambio Stato

Il pattern **Observer** è applicato per disaccoppiare l'hackathon dalla logica di notifica. Le interfacce `HackathonObservable` e `HackathonObserver` permettono a `JudgeNotificationObserver` e `TeamNotificationObserver` di reagire ai cambi di stato senza accoppiamento diretto.

---

## Iterazioni

| Iterazione | Casi d'uso implementati |
|---|---|
| **IT1** | Crea hackathon · Iscrive team ad hackathon · Invia sottomissione · Proclama vincitore · Rilascio valutazione |
| **IT2** | Crea team · Aggiorna sottomissione · Aggiungere mentori · Propone call al team |
| **IT3** | Gestione invito · Abbandona team · Invita al team · Elimina team · Disiscrivi team · Gestione supporto mentore · Prenotare slot · Visualizza richieste supporto · Segnala team · Gestione violazioni |
| **IT4** | Modifica hackathon · Elimina hackathon · Erogazione premio in denaro · Modifica valutazione · Consulta lista hackathon · Visualizza sottomissione |
| **IT5** | Registrarsi al sistema · Autenticarsi al sistema · Recupero credenziali · Gestione profilo |

---

## Setup e Avvio

### Prerequisiti

- Java 17 o superiore
- Maven 3.x
- IntelliJ IDEA (consigliato)

### Avvio

**1. Clona il repository:**
```bash
git clone https://github.com/LorenzoFino/HackHub.git
cd HackHub/springboot
```

**2. Avvia con Maven:**
```bash
mvn spring-boot:run
```

**Oppure con IntelliJ:**
1. Apri la cartella `springboot` come progetto Maven
2. Esegui `HackHubApplication.java`

**Il server è avviato correttamente quando compare:**
[DataInitializer] Test data loaded successfully
Started HackHubApplication in X.XXX seconds
Il server è raggiungibile su **http://localhost:8080**

### Dati di Test

Il `DataInitializer` popola automaticamente il database ad ogni avvio con:

| Ruolo | Email | Password |
|---|---|---|
| Organizzatore | mario@hackhub.com | password |
| Giudice | anna@hackhub.com | password |
| Mentore | luca@hackhub.com | password |
| Utente | giuseppe@mail.com | password |
| Utente | sara@mail.com | password |
| Utente | marco@mail.com | password |

**Hackathon precaricati:**
- Hackathon 1 — stato SUBSCRIPTION
- Hackathon 2 — stato PROGRESS (con submission di test)
- Hackathon 3 — stato EVALUATION (dopo ~2 minuti dallo scheduler)

> ⚠️ Il database H2 è in-memory con `create-drop`: si resetta ad ogni riavvio.

---

## API REST — Postman

La collection Postman completa è disponibile nel file `HackHub_postman_collection.json` nella root del repository.

### Autenticazione

Tutti gli endpoint (eccetto `GET /hackathons` e `/auth/**`) richiedono un Bearer Token JWT.

**Ottieni il token:**
POST http://localhost:8080/api/v1/auth/login
Body: { "email": "mario@hackhub.com", "password": "password" }
Copia il campo `token` dalla risposta e inseriscilo in Postman sotto **Authorization → Bearer Token**.

### Endpoint Principali

| Metodo | Endpoint | Descrizione |
|---|---|---|
| POST | `/api/v1/auth/register` | Registrazione nuovo utente |
| POST | `/api/v1/auth/login` | Login e ottenimento token JWT |
| POST | `/api/v1/auth/forgot-password` | Recupero password via email |
| GET | `/api/v1/hackathons` | Lista tutti gli hackathon |
| POST | `/api/v1/hackathons` | Crea nuovo hackathon |
| PUT | `/api/v1/hackathons/{id}` | Modifica hackathon |
| DELETE | `/api/v1/hackathons/{id}` | Elimina hackathon |
| POST | `/api/v1/hackathons/{id}/winner` | Proclama vincitore |
| POST | `/api/v1/teams` | Crea team |
| POST | `/api/v1/teams/{name}/invitations` | Invia invito |
| POST | `/api/v1/submissions` | Invia sottomissione |
| POST | `/api/v1/valuations` | Rilascia valutazione |
| POST | `/api/v1/reports` | Segnala team |
| POST | `/api/v1/support-requests` | Invia richiesta supporto |
| POST | `/api/v1/calls` | Propone call |

---

## Struttura del Repository
HackHub/
├── springboot/                    # Progetto Spring Boot
│   └── src/main/java/unicam/hackhub/
│       ├── domain/                # Entità, repository, eccezioni, pattern State/Observer
│       ├── application/           # Service layer, DTO command/result, mapper
│       ├── infrastructure/        # JPA, adapter esterni, sicurezza JWT, scheduler
│       └── presentation/          # Controller REST, DTO request/response, mapper
├── uml/
│   └── Progetto_HackHub.vpp       # Modello UML Visual Paradigm (tutte le iterazioni)
├── HackHub_postman_collection.json
└── README.md

---

## Diagrammi UML

Il file `uml/Progetto_HackHub.vpp` contiene il modello UML completo organizzato per iterazione:

- **Diagrammi dei casi d'uso** — attori e funzionalità per iterazione
- **Diagrammi delle classi di analisi** — entità concettuali e relazioni
- **Diagrammi delle classi di progetto** — struttura tecnica con package e dipendenze
- **Diagrammi di sequenza** — flusso di interazione per i principali scenari

Per visualizzare il file è necessario **Visual Paradigm** (edizione Community o superiore).

---

---

# 🇬🇧 HackHub — English

## Table of Contents

- [Project Description](#project-description)
- [Architecture](#architecture)
- [Design Patterns Applied](#design-patterns-applied)
- [Iterations](#iterations)
- [Setup and Running](#setup-and-running)
- [REST API — Postman](#rest-api--postman)

---

## Project Description

**HackHub** is a software platform for the complete lifecycle management of a university hackathon. The system supports the main actors involved — organizers, participants (team members), mentors and judges — and covers all operational phases: from publication and registration, through project submission, to evaluation and winner announcement.

The project was developed as part of the **Software Engineering** course at the **University of Camerino**, following the **Unified Process (UP)** structured in five incremental iterations. For each iteration the following artifacts were produced:

- Use case diagram
- Analysis and design class diagram
- Sequence diagrams for the main use cases
- Corresponding Java implementation

---

## Architecture

The system adopts a layered architecture with clear separation of concerns:
Presentation  →  Application  →  Domain  →  Infrastructure
| Layer | Responsibility |
|---|---|
| **presentation** | REST controllers, request/response DTOs, mappers, HTTP exception handling |
| **application** | Service layer, business logic orchestration, command/result DTOs |
| **domain** | Domain entities, repository interfaces, custom exceptions, State/Observer patterns |
| **infrastructure** | JPA implementations, external adapters (Calendar, Payment, Email), JWT security |

### Technology Stack

| Component | Technology |
|---|---|
| Language | Java 17 |
| Framework | Spring Boot 4.0 |
| Database | H2 (in-memory, create-drop) |
| Security | Spring Security + JWT |
| Build tool | Maven |
| UML | Visual Paradigm (`.vpp`) |

---

## Design Patterns Applied

### State — Hackathon Lifecycle

The hackathon lifecycle is modeled with the **State** pattern. The `Hackathon` class acts as the Context and delegates operations to the current state:
Registration → InProgress → InEvaluation → Concluded
Each state defines which operations are permitted and throws appropriate exceptions for invalid transitions. State transitions happen automatically via `HackathonScheduler` (Tempo actor) or manually by the organizer (winner proclamation).

### Observer — State Change Notifications

The **Observer** pattern is applied to decouple the hackathon from notification logic. The `HackathonObservable` and `HackathonObserver` interfaces allow `JudgeNotificationObserver` and `TeamNotificationObserver` to react to state changes without direct coupling.

---

## Iterations

| Iteration | Implemented use cases |
|---|---|
| **IT1** | Create hackathon · Register team to hackathon · Submit project · Proclaim winner · Release valuation |
| **IT2** | Create team · Update submission · Add mentors · Propose call to team |
| **IT3** | Manage invitation · Leave team · Invite to team · Delete team · Unregister team · Mentor support management · Book slot · View support requests · Report team · Manage violations |
| **IT4** | Edit hackathon · Delete hackathon · Prize disbursement · Edit valuation · Browse hackathon list · View submission |
| **IT5** | Register to system · Authenticate to system · Password recovery · Profile management |

---

## Setup and Running

### Prerequisites

- Java 17 or higher
- Maven 3.x
- IntelliJ IDEA (recommended)

### Running

**1. Clone the repository:**
```bash
git clone https://github.com/LorenzoFino/HackHub.git
cd HackHub/springboot
```

**2. Start with Maven:**
```bash
mvn spring-boot:run
```

**Or with IntelliJ:**
1. Open the `springboot` folder as a Maven project
2. Run `HackHubApplication.java`

**The server has started correctly when the log shows:**
[DataInitializer] Test data loaded successfully
Started HackHubApplication in X.XXX seconds
The server is available at **http://localhost:8080**

### Test Data

The `DataInitializer` automatically populates the database on every startup with:

| Role | Email | Password |
|---|---|---|
| Organizer | mario@hackhub.com | password |
| Judge | anna@hackhub.com | password |
| Mentor | luca@hackhub.com | password |
| User | giuseppe@mail.com | password |
| User | sara@mail.com | password |
| User | marco@mail.com | password |

**Pre-loaded hackathons:**
- Hackathon 1 — SUBSCRIPTION state
- Hackathon 2 — PROGRESS state (with test submission)
- Hackathon 3 — EVALUATION state (after ~2 minutes from scheduler)

> ⚠️ The H2 database is in-memory with `create-drop`: it resets on every restart.

---

## REST API — Postman

The complete Postman collection is available in the file `HackHub_postman_collection.json` in the repository root.

### Authentication

All endpoints (except `GET /hackathons` and `/auth/**`) require a JWT Bearer Token.

**Get the token:**
POST http://localhost:8080/api/v1/auth/login
Body: { "email": "mario@hackhub.com", "password": "password" }
Copy the `token` field from the response and paste it in Postman under **Authorization → Bearer Token**.

### Main Endpoints

| Method | Endpoint | Description |
|---|---|---|
| POST | `/api/v1/auth/register` | Register new user |
| POST | `/api/v1/auth/login` | Login and get JWT token |
| POST | `/api/v1/auth/forgot-password` | Password recovery |
| GET | `/api/v1/hackathons` | List all hackathons |
| POST | `/api/v1/hackathons` | Create new hackathon |
| PUT | `/api/v1/hackathons/{id}` | Edit hackathon |
| DELETE | `/api/v1/hackathons/{id}` | Delete hackathon |
| POST | `/api/v1/hackathons/{id}/winner` | Proclaim winner |
| POST | `/api/v1/teams` | Create team |
| POST | `/api/v1/teams/{name}/invitations` | Send invitation |
| POST | `/api/v1/submissions` | Submit project |
| POST | `/api/v1/valuations` | Release valuation |
| POST | `/api/v1/reports` | Report team |
| POST | `/api/v1/support-requests` | Send support request |
| POST | `/api/v1/calls` | Propose call |

---

## Repository Structure
HackHub/
├── springboot/                    # Spring Boot project
│   └── src/main/java/unicam/hackhub/
│       ├── domain/                # Entities, repositories, exceptions, State/Observer patterns
│       ├── application/           # Service layer, command/result DTOs, mappers
│       ├── infrastructure/        # JPA, external adapters, JWT security, scheduler
│       └── presentation/          # REST controllers, request/response DTOs, mappers
├── uml/
│   └── Progetto_HackHub.vpp       # Visual Paradigm UML model (all iterations)
├── HackHub_postman_collection.json
└── README.md

---

## UML Diagrams

The file `uml/Progetto_HackHub.vpp` contains the complete UML model organized by iteration:

- **Use case diagrams** — actors and features per iteration
- **Analysis class diagrams** — conceptual entities and relationships
- **Design class diagrams** — technical structure with packages and dependencies
- **Sequence diagrams** — interaction flow for the main scenarios

**Visual Paradigm** (Community edition or higher) is required to open the file.
