# Tax Refund API

> A tourist buys a Gucci bag in Paris.  
> They want their VAT back.  
> This is that system.

![CI](https://github.com/Demontrick/tax-refund-api/actions/workflows/ci.yml/badge.svg)

---

# Overview

Global Blue helps tourists reclaim VAT (Value Added Tax) when shopping abroad.

This project is a Spring Boot backend API that simulates a real tax refund claim processing system.

It focuses on the core engineering problems:

- Validate refund eligibility
- Calculate country-specific VAT refunds
- Enforce strict claim status transitions
- Log every state change for audit compliance
- Secure endpoints using JWT roles
- Support PostgreSQL (prod) and H2 (tests)

---

# Tech Stack

- Java 17
- Spring Boot 3
- Spring Security + JWT
- Spring Data JPA + Hibernate
- PostgreSQL
- H2 Database
- Lombok
- JUnit 5 + Mockito
- GitHub Actions CI

---

# Project Structure

src/main/java/com/globalblue/taxrefund/

├── controller  
│   ├── AuthController.java  
│   └── RefundController.java  

├── dto  
│   ├── RefundRequest.java  
│   └── RefundClaimResponse.java  

├── models  
│   ├── RefundClaim.java  
│   ├── AuditLog.java  
│   └── ClaimStatus.java  

├── repository  
│   ├── RefundClaimRepository.java  
│   └── AuditLogRepository.java  

├── security  
│   ├── JwtUtil.java  
│   ├── JwtFilter.java  
│   └── SecurityConfig.java  

├── service  
│   ├── RefundService.java  
│   ├── EligibilityService.java  
│   ├── CurrencyService.java  
│   └── ClaimStateValidator.java  

└── TaxRefundApplication.java

---

# Business Rules

## Eligibility Rules

A refund claim is valid only when:

- Minimum purchase amount = €175
- Country supported:

| Country | Code |
|--------|------|
| France | FR |
| Germany | DE |
| Italy | IT |
| Spain | ES |
| Portugal | PT |
| Switzerland | CH |
| United Kingdom | GB |

---

# VAT Rates

| Country | VAT Rate |
|--------|----------|
| FR | 20% |
| DE | 19% |
| IT | 22% |
| ES | 21% |
| PT | 23% |
| CH | 7.7% |
| GB | 20% |

---

# Refund Formula

refundAmount = purchaseAmount × vatRate / (1 + vatRate) × 0.85

Global Blue retains 15% processing fee.

Example:

€1000 purchase in France

Refund = €141.67

---

# Claim Status Machine

| Current | Next Allowed |
|--------|--------------|
| SUBMITTED | VALIDATED |
| VALIDATED | APPROVED / REJECTED |
| APPROVED | PAID |
| REJECTED | Final |
| PAID | Final |

Flow:

SUBMITTED → VALIDATED → APPROVED → PAID  
                     ↘ REJECTED

---

# Security

JWT-based stateless authentication.

Roles:

- TOURIST
- AGENT
- ADMIN

Protected endpoints require Bearer token.

Generate test token:

POST /api/auth/token

```json
{
  "username": "admin",
  "role": "ADMIN"
}

API Endpoints
Method	Endpoint	Description	Role
POST	/api/auth/token	Generate JWT token	Public
POST	/api/refunds	Create refund claim	Authenticated
GET	/api/refunds/{id}	Get claim by ID	Authenticated
GET	/api/refunds	List all claims	AGENT / ADMIN
POST	/api/refunds/{id}/validate	Validate claim	AGENT / ADMIN
POST	/api/refunds/{id}/approve	Approve and auto-pay	AGENT / ADMIN
POST	/api/refunds/{id}/reject	Reject claim	AGENT / ADMIN
GET	/api/refunds/{id}/audit	Audit history	ADMIN
Example Create Claim

POST /api/refunds

{
  "touristName": "John Smith",
  "purchaseAmount": 1000,
  "purchaseCurrency": "EUR",
  "countryOfPurchase": "FR"
}

Response:

{
  "id": 1,
  "claimReference": "GBL-A1B2C3D4",
  "refundAmount": 141.67,
  "status": "SUBMITTED"
}
Running Locally
1. Set JWT Secret
export JWT_SECRET=dGVzdFNlY3JldEtleUZvclRlc3RpbmdQdXJwb3Nlc09ubHkxMjM=
2. Start PostgreSQL

Create DB:

CREATE DATABASE taxrefund_db;
3. Run App
mvn spring-boot:run

Runs on:

http://localhost:8080

Swagger:

http://localhost:8080/swagger-ui/index.html

Running Tests
mvn test

Uses:

H2 in-memory database
application-test.yml
CI Pipeline

GitHub Actions automatically runs:

Build
Compile
Tests
Package

On every push / pull request to main.

Why This Project Matters

This project models a real financial workflow where correctness matters:

money calculation
rule enforcement
immutable audit trail
secure access
clean architecture

Exactly the kind of engineering challenge companies like Global Blue solve daily.

Author

Built to demonstrate production-grade financial 
workflow engineering — correctness, auditability, 
and clean architecture at the service layer.
