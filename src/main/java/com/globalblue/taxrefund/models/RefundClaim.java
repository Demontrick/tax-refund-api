package com.globalblue.taxrefund.models;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "refund_claims")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RefundClaim {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String claimReference;

    private String touristName;

    private BigDecimal purchaseAmount;

    private String purchaseCurrency;

    private BigDecimal vatRate;

    private BigDecimal refundAmount;

    private String countryOfPurchase;

    @Enumerated(EnumType.STRING)
    private ClaimStatus status;

    private String rejectionReason;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}