# Tax Refund API

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
