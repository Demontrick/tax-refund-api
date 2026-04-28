package com.globalblue.taxrefund.service;

import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Map;

@Service
public class CurrencyService {

    private static final Map<String, BigDecimal> VAT_RATES = Map.of(
            "FR", new BigDecimal("0.20"),
            "DE", new BigDecimal("0.19"),
            "IT", new BigDecimal("0.22"),
            "ES", new BigDecimal("0.21"),
            "PT", new BigDecimal("0.23"),
            "CH", new BigDecimal("0.077"),
            "GB", new BigDecimal("0.20")
    );

    public BigDecimal getVatRate(String country) {

        BigDecimal rate = VAT_RATES.get(country);

        if (rate == null) {
            throw new IllegalArgumentException("Unsupported country: " + country);
        }

        return rate;
    }

    public BigDecimal calculateRefund(BigDecimal amount, BigDecimal vatRate) {

        BigDecimal base = amount.multiply(vatRate)
                .divide(BigDecimal.ONE.add(vatRate), 10, RoundingMode.HALF_UP);

        // Global Blue keeps 15%
        return base.multiply(new BigDecimal("0.85"))
                .setScale(2, RoundingMode.HALF_UP);
    }
}