package com.payments.service;

import com.payments.dto.PaymentCreateRequest;
import com.payments.dto.PaymentIdListResponse;
import com.payments.dto.PaymentResponse;

import java.math.BigDecimal;
import java.util.UUID;

public interface PaymentService {

    UUID createPayment(PaymentCreateRequest request, String clientIp);

    void cancelPayment(UUID id);

    PaymentIdListResponse queryPayments(Boolean onlyActive,
                                        BigDecimal minAmount,
                                        BigDecimal maxAmount);

    PaymentResponse getPayment(UUID id);
}
