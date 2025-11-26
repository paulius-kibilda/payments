package com.payments.service.exception;

public class PaymentCancellationException extends RuntimeException {

    public PaymentCancellationException(String message) {
        super(message);
    }
}
