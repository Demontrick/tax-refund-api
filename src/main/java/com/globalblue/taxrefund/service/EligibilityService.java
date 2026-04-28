package com.globalblue.taxrefund.service;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class EligibilityService {

    private static final double MIN_PURCHASE_AMOUNT = 175.0;

    private static final List<String> VALID_COUNTRIES = List.of(
            "FR", "DE", "IT", "ES", "PT", "CH", "GB"
    );

    private static final Map<String, String> COUNTRY_MAP = Map.of(
            "FRANCE", "FR",
            "GERMANY", "DE",
            "ITALY", "IT",
            "SPAIN", "ES",
            "PORTUGAL", "PT",
            "SWITZERLAND", "CH",
            "UNITED KINGDOM", "GB",
            "UK", "GB"
    );

    public void validateEligibility(double amount, String country) {

        if (amount < MIN_PURCHASE_AMOUNT) {
            throw new BusinessException("Minimum purchase amount is €175");
        }

        String normalizedCountry = normalizeCountry(country);

        if (!VALID_COUNTRIES.contains(normalizedCountry)) {
            throw new BusinessException(
                    "Country not eligible for Tax-Free Shopping: " + country
            );
        }
    }

    private String normalizeCountry(String country) {
        if (country == null || country.isBlank()) {
            return "";
        }

        String cleaned = country.trim().toUpperCase();
        return COUNTRY_MAP.getOrDefault(cleaned, cleaned);
    }
}