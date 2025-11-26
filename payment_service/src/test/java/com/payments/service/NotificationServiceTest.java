package com.payments.service;

import com.payments.config.NotificationProperties;
import com.payments.domain.model.Notification;
import com.payments.domain.model.Payment;
import com.payments.domain.model.PaymentType;
import com.payments.domain.repository.NotificationRepository;
import com.payments.external.NotificationClient;
import com.payments.service.NotificationServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class NotificationServiceTest {

    @Mock
    private NotificationClient notificationClient;

    @Mock
    private NotificationRepository notificationRepository;

    @Mock
    private NotificationProperties notificationProperties;

    @InjectMocks
    private NotificationServiceImpl notificationService;

    private static final String TYPE1_URL = "http://localhost:8080/external/type1?payment_id=";
    private static final String TYPE2_URL = "http://localhost:8080/external/type2?payment_id=";
    private static final String TYPE1_CANCEL_URL = "http://localhost:8080/external/type1/cancel?payment_id=";
    private static final String TYPE2_CANCEL_URL = "http://localhost:8080/external/type2/cancel?payment_id=";

    @Test
    void notifyType1() {
        Payment payment = new Payment();
        payment.setId(UUID.randomUUID());
        payment.setType(PaymentType.TYPE1);

        when(notificationProperties.getType1Url()).thenReturn(TYPE1_URL);

        when(notificationClient.notify(TYPE1_URL + payment.getId())).thenReturn(true);

        notificationService.notifyPaymentSaved(payment);

        verify(notificationRepository, times(1)).save(argThat(n ->
                n.isSuccess() && n.getPayment().equals(payment)
        ));
    }

    @Test
    void notifyType2() {
        Payment payment = new Payment();
        payment.setId(UUID.randomUUID());
        payment.setType(PaymentType.TYPE2);

        when(notificationProperties.getType2Url()).thenReturn(TYPE2_URL);

        when(notificationClient.notify(TYPE2_URL + payment.getId())).thenReturn(true);

        notificationService.notifyPaymentSaved(payment);

        verify(notificationRepository, times(1)).save(argThat(n ->
                n.isSuccess() && n.getPayment().equals(payment)
        ));
    }

    @Test
    void cancelType1() {
        Payment payment = new Payment();
        payment.setId(UUID.randomUUID());
        payment.setType(PaymentType.TYPE1);

        when(notificationProperties.getType1CancelUrl()).thenReturn(TYPE1_CANCEL_URL);

        when(notificationClient.notify(TYPE1_CANCEL_URL + payment.getId())).thenReturn(true);

        notificationService.notifyPaymentCancelled(payment);

        verify(notificationRepository, times(1)).save(argThat(n ->
                n.isSuccess() && n.getPayment().equals(payment)
        ));
    }

    @Test
    void cancelType2() {
        Payment payment = new Payment();
        payment.setId(UUID.randomUUID());
        payment.setType(PaymentType.TYPE2);

        when(notificationProperties.getType2CancelUrl()).thenReturn(TYPE2_CANCEL_URL);

        when(notificationClient.notify(TYPE2_CANCEL_URL + payment.getId())).thenReturn(true);

        notificationService.notifyPaymentCancelled(payment);

        verify(notificationRepository, times(1)).save(argThat(n ->
                n.isSuccess() && n.getPayment().equals(payment)
        ));
    }
}