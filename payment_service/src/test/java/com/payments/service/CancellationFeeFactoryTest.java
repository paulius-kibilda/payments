package com.payments.service;

import com.payments.config.CancellationFeeProperties;
import com.payments.domain.model.Payment;
import com.payments.domain.model.PaymentType;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CancellationFeeFactoryTest {

    private CancellationFeeFactory cancellationFeeFactory;

    @BeforeEach
    void setUp() {
        // configure coefficients (k1=0.05, k2=0.10, k3=0.15)
        CancellationFeeProperties props = new CancellationFeeProperties();
        props.setType1Fee(new BigDecimal("0.05"));
        props.setType2Fee(new BigDecimal("0.10"));
        props.setType3Fee(new BigDecimal("0.15"));

        // use same constructor Spring will use (@RequiredArgsConstructor)
        cancellationFeeFactory = new CancellationFeeFactory(props);
    }

    @Test
    void type1CancellationFee() {
        Payment payment = new Payment();
        payment.setCreatedAt(LocalDateTime.of(2025, 11, 24, 10, 0));

        LocalDateTime now = LocalDateTime.of(2025, 11, 24, 15, 30);

        BigDecimal fee = cancellationFeeFactory.forType(PaymentType.TYPE1)
                .calculate(payment, now);

        assertEquals(new BigDecimal("0.25"), fee);
    }

    @Test
    void type2CancellationFee() {
        Payment payment = new Payment();
        payment.setCreatedAt(LocalDateTime.of(2025, 11, 24, 7, 0));

        LocalDateTime now = LocalDateTime.of(2025, 11, 24, 15, 0);

        BigDecimal fee = cancellationFeeFactory.forType(PaymentType.TYPE2)
                .calculate(payment, now);

        assertEquals(new BigDecimal("0.80"), fee);
    }

    @Test
    void type3CancellationFee() {
        Payment payment = new Payment();
        payment.setCreatedAt(LocalDateTime.of(2025, 11, 24, 8, 51));

        LocalDateTime now = LocalDateTime.of(2025, 11, 24, 15, 50);

        BigDecimal fee = cancellationFeeFactory.forType(PaymentType.TYPE3)
                .calculate(payment, now);

        assertEquals(new BigDecimal("0.90"), fee);
    }
}
