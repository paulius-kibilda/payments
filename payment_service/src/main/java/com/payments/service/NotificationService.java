package com.payments.service;

import com.payments.domain.model.Payment;

public interface NotificationService {

    void notifyPaymentSaved(Payment payment);

    void notifyPaymentCancelled(Payment payment);
}
