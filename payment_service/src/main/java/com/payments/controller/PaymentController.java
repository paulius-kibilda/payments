package com.payments.controller;

import com.payments.dto.PaymentCreateRequest;
import com.payments.dto.PaymentIdListResponse;
import com.payments.dto.PaymentResponse;
import com.payments.service.PaymentService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;
    private final HttpServletRequest request;

    @PostMapping
    public ResponseEntity<Map<String, UUID>> create(@Valid @RequestBody PaymentCreateRequest body) {
        String ip = extractClientIp();
        UUID id = paymentService.createPayment(body, ip);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(Map.of("id", id));
    }

    @PostMapping("/{id}/cancel")
    public ResponseEntity<Void> cancel(@PathVariable UUID id) {
        paymentService.cancelPayment(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public PaymentIdListResponse query(
            @RequestParam(name = "onlyActive", required = false) Boolean onlyActive,
            @RequestParam(name = "minAmount", required = false) BigDecimal minAmount,
            @RequestParam(name = "maxAmount", required = false) BigDecimal maxAmount) {

        return paymentService.queryPayments(onlyActive, minAmount, maxAmount);
    }

    @GetMapping("/{id}")
    public PaymentResponse getById(@PathVariable UUID id) {
        return paymentService.getPayment(id);
    }

    private String extractClientIp() {
        String ipAddress = request.getHeader("X-FORWARDED-FOR");
        if (ipAddress == null) {
            ipAddress = request.getRemoteAddr();
        }
        return ipAddress;
    }
}
