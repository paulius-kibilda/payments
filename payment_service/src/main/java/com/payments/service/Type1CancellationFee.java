package com.payments.service;

import com.payments.config.CancellationFeeProperties;
import com.payments.config.NotificationProperties;
import com.payments.domain.model.Payment;
import com.payments.util.DateTimeUtils;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@RequiredArgsConstructor
public class Type1CancellationFee implements CancellationFee {

    private final CancellationFeeProperties cancellationFeeProperties;

    @Override
    public BigDecimal calculate(Payment payment, LocalDateTime now) {
        long hours = DateTimeUtils.fullHoursBetween(payment.getCreatedAt(), now);
        return cancellationFeeProperties.getType1Fee().multiply(BigDecimal.valueOf(hours));
    }
}
