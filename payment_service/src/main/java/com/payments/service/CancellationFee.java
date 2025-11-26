package com.payments.service;

import com.payments.domain.model.Payment;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public interface CancellationFee {

    BigDecimal calculate(Payment payment, LocalDateTime now);
}
