package com.payments.controller;

import io.swagger.v3.oas.annotations.Hidden;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Hidden
@RestController
@RequestMapping("/external")
public class ExternalController {
    @GetMapping("/type1")
    public ResponseEntity<Void> notifyType1(@RequestParam("payment_id") UUID paymentId) {
        return ResponseEntity.ok().build();
    }

    @GetMapping("/type2")
    public ResponseEntity<Void> notifyType2(@RequestParam("payment_id") UUID paymentId) {
        return ResponseEntity.ok().build();
    }

    @GetMapping("/type1/cancel")
    public ResponseEntity<Void> cancelType1(@RequestParam("payment_id") UUID paymentId) {
        return ResponseEntity.ok().build();
    }

    @GetMapping("/type2/cancel")
    public ResponseEntity<Void> cancelType2(@RequestParam("payment_id") UUID paymentId) {
        return ResponseEntity.ok().build();
    }
}
