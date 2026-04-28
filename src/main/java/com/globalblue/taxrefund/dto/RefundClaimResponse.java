package com.globalblue.taxrefund.controller;

import com.globalblue.taxrefund.dto.RefundClaimResponse;
import com.globalblue.taxrefund.dto.RefundRequest;
import com.globalblue.taxrefund.models.AuditLog;
import com.globalblue.taxrefund.models.RefundClaim;
import com.globalblue.taxrefund.repository.AuditLogRepository;
import com.globalblue.taxrefund.repository.RefundClaimRepository;
import com.globalblue.taxrefund.service.RefundService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/refunds")
public class RefundController {

    private final RefundService refundService;
    private final RefundClaimRepository refundClaimRepository;
    private final AuditLogRepository auditLogRepository;

    public RefundController(RefundService refundService,
                            RefundClaimRepository refundClaimRepository,
                            AuditLogRepository auditLogRepository) {
        this.refundService = refundService;
        this.refundClaimRepository = refundClaimRepository;
        this.auditLogRepository = auditLogRepository;
    }

    // CREATE CLAIM
    @PostMapping
    public RefundClaimResponse create(@RequestBody RefundRequest request) {
        RefundClaim claim = refundService.createClaim(request);
        return mapToResponse(claim);
    }

    // GET BY ID
    @GetMapping("/{id}")
    public RefundClaimResponse getById(@PathVariable Long id) {
        RefundClaim claim = refundClaimRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Claim not found: " + id));
        return mapToResponse(claim);
    }

    // GET ALL
    @GetMapping
    public List<RefundClaimResponse> getAll() {
        return refundClaimRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    // VALIDATE
    @PostMapping("/{id}/validate")
    public RefundClaimResponse validate(@PathVariable Long id) {
        return mapToResponse(refundService.validate(id));
    }

    // APPROVE
    @PostMapping("/{id}/approve")
    public RefundClaimResponse approve(@PathVariable Long id) {
        return mapToResponse(refundService.approve(id));
    }

    // REJECT
    @PostMapping("/{id}/reject")
    public RefundClaimResponse reject(@PathVariable Long id,
                                      @RequestParam String reason) {
        return mapToResponse(refundService.reject(id, reason));
    }

    // AUDIT
    @GetMapping("/{id}/audit")
    public List<AuditLog> audit(@PathVariable Long id) {
        return auditLogRepository.findByClaimId(id);
    }

    // MAPPER (simple manual mapping for POC)
    private RefundClaimResponse mapToResponse(RefundClaim claim) {
        RefundClaimResponse res = new RefundClaimResponse();
        res.setId(claim.getId());
        res.setClaimReference(claim.getClaimReference());
        res.setTouristName(claim.getTouristName());
        res.setPurchaseAmount(claim.getPurchaseAmount());
        res.setPurchaseCurrency(claim.getPurchaseCurrency());
        res.setVatRate(claim.getVatRate());
        res.setRefundAmount(claim.getRefundAmount());
        res.setCountryOfPurchase(claim.getCountryOfPurchase());
        res.setStatus(claim.getStatus());
        res.setRejectionReason(claim.getRejectionReason());
        res.setCreatedAt(claim.getCreatedAt());
        res.setUpdatedAt(claim.getUpdatedAt());
        return res;
    }
}