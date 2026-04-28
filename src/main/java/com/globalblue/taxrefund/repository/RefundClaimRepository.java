package com.globalblue.taxrefund.repository;

import com.globalblue.taxrefund.models.RefundClaim;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RefundClaimRepository extends JpaRepository<RefundClaim, Long> {

    Optional<RefundClaim> findByClaimReference(String claimReference);
}