package com.globalblue.taxrefund.service;

import com.globalblue.taxrefund.models.ClaimStatus;
import org.springframework.stereotype.Component;

@Component
public class ClaimStateValidator {

    public void validateTransition(ClaimStatus current, ClaimStatus next) {

        if (current == ClaimStatus.REJECTED || current == ClaimStatus.PAID) {
            throw new IllegalStateException("Cannot modify final state: " + current);
        }

        switch (next) {

            case VALIDATED -> {
                if (current != ClaimStatus.SUBMITTED) {
                    throw new IllegalStateException("Only SUBMITTED claims can be validated");
                }
            }

            case APPROVED -> {
                if (current != ClaimStatus.VALIDATED) {
                    throw new IllegalStateException("Only VALIDATED claims can be approved");
                }
            }

            case PAID -> {
                if (current != ClaimStatus.APPROVED) {
                    throw new IllegalStateException("Only APPROVED claims can be paid");
                }
            }

            case REJECTED -> {
                if (current == ClaimStatus.PAID) {
                    throw new IllegalStateException("Paid claims cannot be rejected");
                }
            }

            default -> { }
        }
    }
}