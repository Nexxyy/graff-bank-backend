<img src="https://i.imgur.com/u0tCqfH.png">

# Graff Bank

> ⚠️ **PROJECT UNDER DEVELOPMENT — NOT A FINAL PRODUCT**

**Graff Bank** is a fictional banking platform built as an open-source software engineering project.

The backend, **Ledger**, is being developed around concepts commonly found in ledger-based financial systems, with a focus on architecture, transaction consistency, concurrency, idempotency, asynchronous processing, security, and data integrity.

The project includes both a **backend API and a UI**, which are being developed together as the platform evolves.

**Graff Bank is not a real bank, financial institution, or production-ready banking system.** It is an experimental environment for exploring how financial software can be designed and implemented.

---

## 🧭 About the Project

The name **Graff Bank** represents the fictional banking institution used throughout the project.

**Ledger** is the backend system behind Graff Bank and the name reflects one of the core concepts explored by the project: maintaining reliable financial records and consistency across transactions.

The goal is not to simulate an entire real-world bank, but to build a realistic enough environment to explore engineering problems that appear in financial systems.

The project is intentionally being developed in public, with the architecture and implementation evolving over time.

---

## 🎯 Goal

The main goal of Graff Bank is to study and implement problems commonly found in real-world financial systems, such as:

* transaction consistency;
* concurrency;
* idempotency;
* asynchronous processing;
* data integrity;
* authentication and authorization;
* relational persistence;
* cryptography and data signing;
* communication between components;
* fault tolerance;
* separation of responsibilities.

Rather than simply building a CRUD application, the focus is on the **engineering decisions behind financial operations**.

---

## ⚠️ Project Status

Graff Bank and its Ledger backend are currently under **active development**.

Some features are already implemented, while others are still being designed, refined, or planned.

| Area                    | Status            |
| ----------------------- | ----------------- |
| Users                   | 🟢 Implemented    |
| Accounts                | 🟢 Implemented    |
| Authentication          | 🟢 Implemented    |
| Refresh Tokens          | 🟢 Implemented    |
| Transfers               | 🟢 Implemented    |
| Idempotency             | 🟢 Implemented    |
| Account Locking         | 🟢 Implemented    |
| Transfer Signatures     | 🟢 Implemented    |
| Loans                   | 🟢 Implemented    |
| Asynchronous Processing | 🟢 Implemented    |
| Outbox Pattern          | 🟢 Implemented    |
| Cards                   | 🟡 Experimental   |
| Invoices                | 🔴 Planned        |
| Automated Tests         | 🟡 In Progress    |
| Observability           | 🔴 Planned        |
| Graff Bank UI           | 🟡 In Development |

The statuses above represent the current state of the project and may change as development continues.

---

## 🖥️ Graff Bank UI

The **Graff Bank UI** is being developed alongside the Ledger backend.

Its purpose is to provide a visual banking interface for interacting with and demonstrating the platform.

The UI is also **not a finished product** and is evolving together with the backend.

Current and planned areas include:

* authentication;
* account overview;
* transfers;
* loans;
* financial information;
* transaction history;
* banking workflows.

The backend and its engineering challenges remain the primary focus of the project, while the UI provides the interface through which the fictional bank can be explored.

---

# 🏗️ Architecture

The Ledger backend uses **Hexagonal Architecture (Ports and Adapters)** to keep the domain and application use cases independent from infrastructure details.

The main structure follows:

```text
api/
application/
domain/
configuration/
infrastructure/
```

### API

Responsible for the HTTP interface of Graff Bank.

```text
api/controller
api/dto
```

Controllers handle HTTP concerns and delegate execution to application use cases.

### Application

Contains application logic and use-case orchestration.

```text
application/
├── port/
│   └── out/
├── usecase/
│   ├── account/
│   ├── auth/
│   ├── loan/
│   ├── transaction/
│   └── user/
```

Ports define contracts that allow the application layer to remain decoupled from infrastructure implementations.

For example:

```kotlin
interface PasswordGateway {

    fun salty(secret: String): String?

    fun matches(raw: String, encoded: String): Boolean
}
```

The infrastructure provides the implementation:

```kotlin
@Component
class BCryptHasher : PasswordGateway {

    private val encoder = BCryptPasswordEncoder()

    override fun salty(secret: String): String? =
        encoder.encode(secret)

    override fun matches(
        raw: String,
        encoded: String
    ): Boolean =
        encoder.matches(raw, encoded)
}
```

The use case therefore depends only on the `PasswordGateway` contract rather than directly depending on BCrypt.

---

# 💸 Transfers

Transfers are one of the main engineering areas of Ledger.

The flow involves:

```text
Request
   │
   ▼
Idempotency
   │
   ▼
Account Locking
   │
   ▼
Validation
   │
   ▼
Balance Update
   │
   ▼
Transfer Persistence
   │
   ▼
Cryptographic Signature
```

The implementation explores problems that arise when multiple concurrent requests can modify the same account balance.

---

## Idempotency

Transfers use an idempotency key.

The database enforces uniqueness through:

```sql
key VARCHAR(255) NOT NULL UNIQUE
```

Reservation uses:

```sql
ON CONFLICT (key) DO NOTHING
```

This avoids the race-prone pattern:

```text
SELECT
   ↓
not found
   ↓
INSERT
```

when multiple requests arrive concurrently.

Instead, the database's uniqueness constraint becomes part of the consistency mechanism.

The project also stores a request hash, allowing subsequent attempts to be validated against the original request associated with the idempotency key.

---

# 🔒 Concurrency and Locking

Accounts provide dedicated operations for acquiring pessimistic locks:

```sql
SELECT
    id,
    owner_id,
    balance,
    created_at
FROM account
WHERE id = :id
FOR UPDATE
```

The intention is to prevent concurrent transactions from reading and modifying the same balance in an inconsistent manner.

The application contains a dedicated policy for transfer locking:

```text
TransferLockingPolicy
```

The lock acquisition strategy is part of the application design and continues to evolve with the project.

---

# 🔐 Cryptographic Integrity

Transfers contain a cryptographic signature:

```text
signature
```

The project includes dedicated components for:

```text
Ed25519
SHA-256
```

The signature is used as an experimental mechanism to provide integrity guarantees for transfer data.

This implementation is **not intended to replace real-world banking security mechanisms**. The goal is to explore how cryptographic mechanisms can be incorporated into a financial application's architecture.

---

# 📨 Asynchronous Processing

Loans use asynchronous processing through **RabbitMQ**.

The flow is approximately:

```text
HTTP Request
     │
     ▼
RequestLoanUseCase
     │
     ▼
Loan
     │
     ▼
Outbox Event
     │
     ▼
RabbitMQ
     │
     ▼
LoanProcessingConsumer
     │
     ▼
ProcessLoanUseCase
```

This separates loan request creation from its subsequent processing.

---

# 📦 Outbox Pattern

To avoid inconsistencies between database persistence and message publishing, Ledger uses the **Transactional Outbox Pattern**.

Instead of relying directly on:

```text
Database Transaction
        +
RabbitMQ Publish
```

the event is first persisted:

```text
Database
   │
   ├── Business Data
   │
   └── Outbox Event
             │
             ▼
        Publisher
             │
             ▼
          RabbitMQ
```

The `outbox_event` table tracks the event lifecycle:

```text
PENDING
PROCESSING
PUBLISHED
```

Processing uses:

```sql
FOR UPDATE SKIP LOCKED
```

allowing multiple workers to claim events without concurrently processing the same record.

---

# 🗄️ Persistence

Ledger uses:

* PostgreSQL
* JDBI
* Explicit SQL

The project **does not use JPA/Hibernate as its primary persistence layer**.

Stores implement ports defined by the application layer:

```text
Application Port
       │
       ▼
     JDBI
       │
       ▼
  PostgreSQL
```

The choice of explicit SQL is intentional: it provides greater control over persistence operations and allows the project to explore database-level consistency mechanisms directly.

---

# 🔑 Authentication

Authentication uses:

* JWT;
* refresh tokens;
* BCrypt;
* Spring Security.

The authentication flow uses ports to keep security mechanisms decoupled from application use cases.

Related components include:

```text
PasswordGateway
TokensGateway
RefreshTokenGateway
```

---

# 🏦 Domain

The Ledger domain currently includes concepts related to:

```text
User
Account
Transfer
TransferIdempotency
Loan
Card
RefreshToken
OutboxEvent
```

States and events are modeled explicitly, including:

```text
LoanStatus
CardStatus
CardType
OutboxStatus
```

The intention is to keep important business rules close to the domain model instead of concentrating them exclusively inside controllers.

---

# 💳 Cards

Card functionality currently exists as an **experimental/demo feature**.

> ⚠️ **NEVER use real card data with this project.**

The current implementation **is not PCI DSS compliant** and is not intended to represent a secure implementation for real-world card processing.

In a real financial system, sensitive information such as card numbers and CVVs would require a substantially different architecture, including appropriate tokenization, data protection, and security controls.

---

# 🛠️ Tech Stack

### Backend

* Kotlin
* Spring Boot
* Spring Security

### Persistence

* PostgreSQL
* JDBI
* SQL

### Messaging

* RabbitMQ
* Transactional Outbox

### Security

* JWT
* BCrypt
* Ed25519
* SHA-256

### Architecture

* Hexagonal Architecture
* Ports and Adapters
* Domain-oriented design
* Use Cases

### UI

* Under active development
* Built alongside the backend
* Banking interface for Graff Bank
* Used to interact with and demonstrate the platform

---

# 📁 Project Structure

```text
src/main/kotlin/com/ledger/nexy/

├── api/
│   ├── controller/
│   └── dto/
│
├── application/
│   ├── port/
│   │   └── out/
│   └── usecase/
│
├── configuration/
│
├── domain/
│   ├── account/
│   ├── auth/
│   ├── card/
│   ├── loan/
│   ├── outbox/
│   ├── transaction/
│   └── user/
│
├── infrastructure/
│   ├── crypto/
│   ├── messaging/
│   ├── persistence/
│   └── security/
│
└── LedgerApplication.kt
```

---

# 🚧 Roadmap

Graff Bank and Ledger are still under development.

Some of the next steps include:

* [ ] Expand automated test coverage
* [ ] Concurrency tests for transfers
* [ ] High-concurrency idempotency tests
* [ ] Improve recovery of stuck `PROCESSING` Outbox events
* [ ] Observability
* [ ] Metrics
* [ ] Auditing
* [ ] Invoice implementation
* [ ] Evolve the card domain
* [ ] Security improvements
* [ ] Continue Graff Bank UI development
* [ ] Architectural documentation
* [ ] ADRs for important architectural decisions
* [ ] Integration tests with PostgreSQL and RabbitMQ

The roadmap does not necessarily represent a fixed implementation order.

---

# 🧪 Project Philosophy

Graff Bank is being developed as a **software engineering laboratory built around a fictional banking system**.

The priority is not simply adding features, but understanding problems such as:

> How can a concurrent transfer avoid corrupting an account balance?

> How can repeated requests be prevented from executing the same financial operation twice?

> How can a message be published without losing consistency between the database and the broker?

> How can business rules remain decoupled from infrastructure?

> How can cryptographic mechanisms be used to provide data integrity?

> How can the database actively participate in consistency guarantees?

These questions guide the evolution of Ledger.

---

# ⚠️ Disclaimer

Graff Bank is a **fictional banking platform** created for educational and experimental purposes.

Ledger is its backend implementation.

The project:

* is not a real bank;
* is not a financial institution;
* must not be used for real financial transactions;
* must not receive real customer data;
* has no regulatory certifications;
* is not PCI DSS compliant;
* should not be considered production-ready.

The backend and UI are both under active development. The code represents the current state of the project and may contain decisions, implementations, or abstractions that will be revised as development continues.