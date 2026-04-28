package com.globalblue.taxrefund.exception;

import com.globalblue.taxrefund.service.BusinessException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<?> handleState(IllegalStateException ex) {
        return ResponseEntity.badRequest()
                .body(Map.of(
                        "error", "INVALID_STATE_TRANSITION",
                        "message", ex.getMessage()
                ));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<?> handleBusiness(IllegalArgumentException ex) {
        return ResponseEntity.badRequest()
                .body(Map.of(
                        "error", "BUSINESS_RULE_VIOLATION",
                        "message", ex.getMessage()
                ));
    }

    // ✅ ADD THIS (IMPORTANT FIX)
    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<?> handleCustomBusiness(BusinessException ex) {
        return ResponseEntity.badRequest()
                .body(Map.of(
                        "error", "BUSINESS_RULE_VIOLATION",
                        "message", ex.getMessage()
                ));
    }
}