package com.globalblue.taxrefund.dto;

import java.math.BigDecimal;

public class RefundRequest {

    private String touristName;
    private BigDecimal purchaseAmount;
    private String purchaseCurrency;
    private String countryOfPurchase;

    public String getTouristName() {
        return touristName;
    }

    public void setTouristName(String touristName) {
        this.touristName = touristName;
    }

    public BigDecimal getPurchaseAmount() {
        return purchaseAmount;
    }

    public void setPurchaseAmount(BigDecimal purchaseAmount) {
        this.purchaseAmount = purchaseAmount;
    }

    public String getPurchaseCurrency() {
        return purchaseCurrency;
    }

    public void setPurchaseCurrency(String purchaseCurrency) {
        this.purchaseCurrency = purchaseCurrency;
    }

    public String getCountryOfPurchase() {
        return countryOfPurchase;
    }

    public void setCountryOfPurchase(String countryOfPurchase) {
        this.countryOfPurchase = countryOfPurchase;
    }
}