package com.payments.service;

import com.payments.config.CancellationFeeProperties;
import com.payments.domain.model.PaymentType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CancellationFeeFactory {

    private final CancellationFeeProperties cancellationFee;

    public CancellationFee forType(PaymentType type) {
        return switch (type) {
            case TYPE1 -> new Type1CancellationFee(cancellationFee);
            case TYPE2 -> new Type2CancellationFee(cancellationFee);
            case TYPE3 -> new Type3CancellationFee(cancellationFee);
        };
    }
}