package com.globalblue.taxrefund.controller;

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

    // CREATE CLAIM (DTO FIXED)
    @PostMapping
    public RefundClaim create(@RequestBody RefundRequest request) {
        return refundService.createClaimFromRequest(request);
    }

    // GET BY ID
    @GetMapping("/{id}")
    public RefundClaim getById(@PathVariable Long id) {
        return refundClaimRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Claim not found: " + id));
    }

    // GET ALL
    @GetMapping
    public List<RefundClaim> getAll() {
        return refundClaimRepository.findAll();
    }

    // VALIDATE
    @PostMapping("/{id}/validate")
    public RefundClaim validate(@PathVariable Long id) {
        return refundService.validate(id);
    }

    // APPROVE → PAID
    @PostMapping("/{id}/approve")
    public RefundClaim approve(@PathVariable Long id) {
        return refundService.approve(id);
    }

    // REJECT
    @PostMapping("/{id}/reject")
    public RefundClaim reject(@PathVariable Long id,
                              @RequestParam String reason) {
        return refundService.reject(id, reason);
    }

    // AUDIT LOGS
    @GetMapping("/{id}/audit")
    public List<AuditLog> audit(@PathVariable Long id) {
        return auditLogRepository.findByClaimId(id);
    }
}