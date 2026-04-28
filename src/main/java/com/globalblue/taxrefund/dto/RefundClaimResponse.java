package com.globalblue.taxrefund.dto;

import com.globalblue.taxrefund.models.ClaimStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class RefundClaimResponse {

    private Long id;
    private String claimReference;
    private String touristName;
    private BigDecimal purchaseAmount;
    private String purchaseCurrency;
    private BigDecimal vatRate;
    private BigDecimal refundAmount;
    private String countryOfPurchase;
    private ClaimStatus status;
    private String rejectionReason;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public RefundClaimResponse() {
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getClaimReference() { return claimReference; }
    public void setClaimReference(String claimReference) { this.claimReference = claimReference; }

    public String getTouristName() { return touristName; }
    public void setTouristName(String touristName) { this.touristName = touristName; }

    public BigDecimal getPurchaseAmount() { return purchaseAmount; }
    public void setPurchaseAmount(BigDecimal purchaseAmount) { this.purchaseAmount = purchaseAmount; }

    public String getPurchaseCurrency() { return purchaseCurrency; }
    public void setPurchaseCurrency(String purchaseCurrency) { this.purchaseCurrency = purchaseCurrency; }

    public BigDecimal getVatRate() { return vatRate; }
    public void setVatRate(BigDecimal vatRate) { this.vatRate = vatRate; }

    public BigDecimal getRefundAmount() { return refundAmount; }
    public void setRefundAmount(BigDecimal refundAmount) { this.refundAmount = refundAmount; }

    public String getCountryOfPurchase() { return countryOfPurchase; }
    public void setCountryOfPurchase(String countryOfPurchase) { this.countryOfPurchase = countryOfPurchase; }

    public ClaimStatus getStatus() { return status; }
    public void setStatus(ClaimStatus status) { this.status = status; }

    public String getRejectionReason() { return rejectionReason; }
    public void setRejectionReason(String rejectionReason) { this.rejectionReason = rejectionReason; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}