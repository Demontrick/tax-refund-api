package com.globalblue.taxrefund.service;

import com.globalblue.taxrefund.dto.RefundRequest;
import com.globalblue.taxrefund.models.*;
import com.globalblue.taxrefund.repository.AuditLogRepository;
import com.globalblue.taxrefund.repository.RefundClaimRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class RefundService {

    private final RefundClaimRepository refundClaimRepository;
    private final AuditLogRepository auditLogRepository;
    private final EligibilityService eligibilityService;
    private final CurrencyService currencyService;
    private final ClaimStateValidator stateValidator;

    public RefundService(RefundClaimRepository refundClaimRepository,
                         AuditLogRepository auditLogRepository,
                         EligibilityService eligibilityService,
                         CurrencyService currencyService,
                         ClaimStateValidator stateValidator) {

        this.refundClaimRepository = refundClaimRepository;
        this.auditLogRepository = auditLogRepository;
        this.eligibilityService = eligibilityService;
        this.currencyService = currencyService;
        this.stateValidator = stateValidator;
    }

    public RefundClaim createClaimFromRequest(RefundRequest request) {

        eligibilityService.validateEligibility(
                request.getPurchaseAmount().doubleValue(),
                request.getCountryOfPurchase()
        );

        String reference = "GBL-" + UUID.randomUUID().toString().substring(0, 8);

        BigDecimal vatRate = currencyService.getVatRate(request.getCountryOfPurchase());

        BigDecimal refundAmount = currencyService.calculateRefund(
                request.getPurchaseAmount(),
                vatRate
        );

        RefundClaim claim = RefundClaim.builder()
                .claimReference(reference)
                .touristName(request.getTouristName())
                .purchaseAmount(request.getPurchaseAmount())
                .purchaseCurrency(request.getPurchaseCurrency())
                .countryOfPurchase(request.getCountryOfPurchase())
                .vatRate(vatRate)
                .refundAmount(refundAmount)
                .status(ClaimStatus.SUBMITTED)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        RefundClaim saved = refundClaimRepository.save(claim);

        logAudit(saved.getId(), "CREATE", null, ClaimStatus.SUBMITTED.name());

        return saved;
    }

    public RefundClaim validate(Long id) {
        RefundClaim claim = getClaim(id);

        stateValidator.validateTransition(claim.getStatus(), ClaimStatus.VALIDATED);

        String prev = claim.getStatus().name();

        claim.setStatus(ClaimStatus.VALIDATED);
        claim.setUpdatedAt(LocalDateTime.now());

        RefundClaim updated = refundClaimRepository.save(claim);

        logAudit(id, "VALIDATE", prev, ClaimStatus.VALIDATED.name());

        return updated;
    }

    public RefundClaim approve(Long id) {
        RefundClaim claim = getClaim(id);

        stateValidator.validateTransition(claim.getStatus(), ClaimStatus.APPROVED);

        String prev = claim.getStatus().name();

        claim.setStatus(ClaimStatus.APPROVED);
        claim.setUpdatedAt(LocalDateTime.now());
        refundClaimRepository.save(claim);

        logAudit(id, "APPROVE", prev, ClaimStatus.APPROVED.name());

        stateValidator.validateTransition(claim.getStatus(), ClaimStatus.PAID);

        claim.setStatus(ClaimStatus.PAID);
        claim.setUpdatedAt(LocalDateTime.now());
        refundClaimRepository.save(claim);

        logAudit(id, "PAY", ClaimStatus.APPROVED.name(), ClaimStatus.PAID.name());

        return claim;
    }

    public RefundClaim reject(Long id, String reason) {
        RefundClaim claim = getClaim(id);

        stateValidator.validateTransition(claim.getStatus(), ClaimStatus.REJECTED);

        String prev = claim.getStatus().name();

        claim.setStatus(ClaimStatus.REJECTED);
        claim.setRejectionReason(reason);
        claim.setUpdatedAt(LocalDateTime.now());

        RefundClaim updated = refundClaimRepository.save(claim);

        logAudit(id, "REJECT", prev, ClaimStatus.REJECTED.name());

        return updated;
    }

    private RefundClaim getClaim(Long id) {
        return refundClaimRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Claim not found: " + id));
    }

    private void logAudit(Long claimId, String action, String prev, String next) {

        AuditLog log = AuditLog.builder()
                .claimId(claimId)
                .action(action)
                .previousStatus(prev)
                .newStatus(next)
                .performedBy("SYSTEM")
                .timestamp(LocalDateTime.now())
                .build();

        auditLogRepository.save(log);
    }
}