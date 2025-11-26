package com.payments.service;

import com.payments.config.NotificationProperties;
import com.payments.domain.model.Notification;
import com.payments.domain.model.Payment;
import com.payments.domain.model.PaymentType;
import com.payments.domain.repository.NotificationRepository;
import com.payments.external.NotificationClient;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {

    private static final Logger log = LoggerFactory.getLogger(NotificationServiceImpl.class);

    private final NotificationClient notificationClient;
    private final NotificationRepository notificationRepository;
    private final NotificationProperties notificationProperties;

    @Override
    public void notifyPaymentSaved(Payment payment) {
        if (payment.getType() == PaymentType.TYPE3) {
            return;
        }

        Notification notification = new Notification();
        notification.setPayment(payment);

        boolean success;

        try {
            if (payment.getType() == PaymentType.TYPE1) {
                notification.setTargetUrl(notificationProperties.getType1Url() + payment.getId());
                success = notificationClient.notify(notification.getTargetUrl());
            } else {
                notification.setTargetUrl(notificationProperties.getType2Url() + payment.getId());
                success = notificationClient.notify(notification.getTargetUrl());
            }

            notification.setSuccess(success);

            if (!success) {
                notification.setErrorMessage("Notification failed did not receive 2xx response");
            }

        } catch (Exception e) {
            log.warn("Failed to notify external service for payment {}: {}",
                    payment.getId(), e.getMessage());
            notification.setSuccess(false);
            notification.setErrorMessage(e.getMessage());
        }

        payment.getNotifications().add(notification);

        notificationRepository.save(notification);
    }

    @Override
    public void notifyPaymentCancelled(Payment payment) {
        if (payment.getType() == PaymentType.TYPE3) {
            return;
        }

        Notification notification = new Notification();
        notification.setPayment(payment);

        boolean success;

        try {
            if (payment.getType() == PaymentType.TYPE1) {
                notification.setTargetUrl(notificationProperties.getType1CancelUrl() + payment.getId());
                success = notificationClient.notify(notification.getTargetUrl());
            } else {
                notification.setTargetUrl(notificationProperties.getType2CancelUrl() + payment.getId());
                success = notificationClient.notify(notification.getTargetUrl());
            }

            notification.setSuccess(success);

            if (!success) {
                notification.setErrorMessage("Notification failed did not receive 2xx response");
            }

        } catch (Exception e) {
            log.warn("Failed to notify external service for payment {}: {}",
                    payment.getId(), e.getMessage());
            notification.setSuccess(false);
            notification.setErrorMessage(e.getMessage());
        }

        payment.getNotifications().add(notification);

        notificationRepository.save(notification);
    }
}
