markdown# Tax Refund API

> A tourist buys a Gucci bag in Paris.  
> They want their VAT back.  
> This is that system.

![CI](https://github.com/Demontrick/tax-refund-api/actions/workflows/ci.yml/badge.svg)

---

## Overview

Global Blue processes Tax-Free Shopping VAT refunds 
for tourists across 50+ countries. This API models 
the core engineering problems behind that system:

- Validate refund eligibility per country and amount
- Calculate VAT refunds with country-specific rates
- Enforce strict claim status transitions at service layer
- Log every state change to an immutable audit trail
- Secure endpoints with JWT role-based access control
- PostgreSQL for production, H2 for tests — same JPA layer

---

## Tech Stack

- Java 17 · Spring Boot 3 · Spring Security
- Spring Data JPA · Hibernate · Lombok
- PostgreSQL · H2 (test)
- JUnit 5 · Mockito
- GitHub Actions CI

---

## Eligibility Rules

| Rule | Value |
|------|-------|
| Minimum purchase | €175 |
| Supported countries | FR, DE, IT, ES, PT, CH, GB |

Invalid claims throw immediately — no silent failures.

---

## VAT Rates by Country

| Country | Code | VAT Rate |
|---------|------|----------|
| France | FR | 20% |
| Germany | DE | 19% |
| Italy | IT | 22% |
| Spain | ES | 21% |
| Portugal | PT | 23% |
| Switzerland | CH | 7.7% |
| United Kingdom | GB | 20% |

---

## Refund Formula
refundAmount = purchaseAmount × vatRate / (1 + vatRate) × 0.85

Global Blue retains 15% processing fee.

Example: €1,000 purchase in France → **€141.67 refund**

---

## Status Machine
SUBMITTED → VALIDATED → APPROVED → PAID
↘ REJECTED

| Transition | Allowed |
|-----------|---------|
| SUBMITTED → VALIDATED | ✓ |
| VALIDATED → APPROVED | ✓ |
| VALIDATED → REJECTED | ✓ |
| APPROVED → PAID | ✓ |
| Any invalid transition | ✗ throws IllegalStateException |

Every transition creates an AuditLog entry automatically.

---

## Endpoints

| Method | Endpoint | Description | Role |
|--------|----------|-------------|------|
| POST | /api/auth/token | Generate JWT | Public |
| POST | /api/refunds | Submit claim | Authenticated |
| GET | /api/refunds/{id} | Get claim | Authenticated |
| GET | /api/refunds | List all claims | AGENT / ADMIN |
| POST | /api/refunds/{id}/validate | Validate | AGENT / ADMIN |
| POST | /api/refunds/{id}/approve | Approve + auto-pay | AGENT / ADMIN |
| POST | /api/refunds/{id}/reject | Reject with reason | AGENT / ADMIN |
| GET | /api/refunds/{id}/audit | Audit trail | ADMIN |

---

## JWT Roles

- **TOURIST** — submit and view own claims
- **AGENT** — validate, approve, reject claims
- **ADMIN** — full access including audit logs

Generate a test token:

```bash
POST /api/auth/token
{
  "username": "admin",
  "role": "ADMIN"
}
```

---

## Example Request

```bash
POST /api/refunds
{
  "touristName": "John Smith",
  "purchaseAmount": 1000,
  "purchaseCurrency": "EUR",
  "countryOfPurchase": "FR"
}
```

Response:
```json
{
  "id": 1,
  "claimReference": "GBL-A1B2C3D4",
  "refundAmount": 141.67,
  "status": "SUBMITTED"
}
```

---

## Running Locally

```bash
# Set JWT secret
export JWT_SECRET=dGVzdFNlY3JldEtleUZvclRlc3RpbmdQdXJwb3Nlc09ubHkxMjM=

# Create database
CREATE DATABASE taxrefund_db;

# Run application
mvn spring-boot:run
```

Runs on `http://localhost:8080`

---

## Running Tests

```bash
mvn test
```

Tests cover: eligibility validation, VAT calculation,
status machine transitions, audit logging, JWT authorization.
H2 in-memory database — no local PostgreSQL needed for tests.

---

## CI

GitHub Actions runs compile + test + package on every push.
PostgreSQL 15 service container. Java 17 temurin. Green on main.
