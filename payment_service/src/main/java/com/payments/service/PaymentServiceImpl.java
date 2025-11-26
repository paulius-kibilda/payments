package com.payments.service;

import com.payments.domain.model.Payment;
import com.payments.domain.repository.PaymentRepository;
import com.payments.dto.PaymentCreateRequest;
import com.payments.dto.PaymentIdListResponse;
import com.payments.dto.PaymentResponse;
import com.payments.service.exception.PaymentCancellationException;
import com.payments.service.exception.PaymentNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;
    private final CancellationFeeFactory cancellationFeeFactory;
    private final PaymentFactory paymentFactory;
    private final NotificationService notificationService;
    private final ClientCountryService clientCountryService;

    @Override
    @Transactional
    public UUID createPayment(PaymentCreateRequest request, String clientIp) {
        String country = clientCountryService.resolveAndLog(clientIp);
        Payment payment = paymentFactory.fromRequest(request, clientIp, country);

        payment = paymentRepository.save(payment);

        notificationService.notifyPaymentSaved(payment);

        return payment.getId();
    }

    @Override
    @Transactional
    public void cancelPayment(UUID id) {
        Payment payment = paymentRepository.findById(id)
                .orElseThrow(() -> new PaymentNotFoundException("Payment not found: " + id));

        LocalDateTime now = LocalDateTime.now();

        if (!payment.isCancelableAt(now)) {
            throw new PaymentCancellationException("Cannot cancel the payment on the next day");
        }

        if (payment.isCanceled()) {
            throw new PaymentCancellationException("Payment has already been canceled");
        }

        BigDecimal fee = cancellationFeeFactory.forType(payment.getType())
                .calculate(payment, now);

        payment.setCanceled(true);
        payment.setCancelFee(fee);
        payment.setCanceledAt(now);

        paymentRepository.save(payment);

        notificationService.notifyPaymentCancelled(payment);
    }

    @Override
    @Transactional(readOnly = true)
    public PaymentIdListResponse queryPayments(Boolean onlyActive,
                                               BigDecimal minAmount,
                                               BigDecimal maxAmount) {

        List<Payment> payments = paymentRepository.findForQuery(onlyActive, minAmount, maxAmount);
        List<UUID> ids = payments.stream().map(Payment::getId).toList();
        return new PaymentIdListResponse(ids);
    }

    @Override
    @Transactional(readOnly = true)
    public PaymentResponse getPayment(UUID id) {
        Payment payment = paymentRepository.findById(id)
                .orElseThrow(() -> new PaymentNotFoundException("Payment not found: " + id));

        LocalDateTime now = LocalDateTime.now();
        BigDecimal fee = cancellationFeeFactory.forType(payment.getType())
                .calculate(payment, now);

        return new PaymentResponse(payment.getId(), payment.isCanceled(), fee);
    }
}
