package com.payments.service;

import com.payments.domain.model.Currency;
import com.payments.domain.model.PaymentType;
import com.payments.dto.PaymentCreateRequest;
import com.payments.service.exception.PaymentValidationException;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class PaymentFactoryTest {

    private final PaymentFactory factory = new PaymentFactory();

    @Test
    void type1RequiresEur() {
        PaymentCreateRequest req = new PaymentCreateRequest();
        req.setType(PaymentType.TYPE1);
        req.setCreditorIban("LT123");
        req.setDebtorIban("LT345");
        req.setDetails("test");
        req.setCurrency(Currency.USD);

        assertThrows(PaymentValidationException.class,
                () -> factory.fromRequest(req, "127.0.0.1", "LT"));
    }

    @Test
    void type1RequiresDetails() {
        PaymentCreateRequest req = new PaymentCreateRequest();
        req.setType(PaymentType.TYPE1);
        req.setCreditorIban("LT123");
        req.setDebtorIban("LT345");
        req.setCurrency(Currency.EUR);

        assertThrows(PaymentValidationException.class,
                () -> factory.fromRequest(req, "127.0.0.1", "LT"));
    }

    @Test
    void type2RequiresUSD() {
        PaymentCreateRequest req = new PaymentCreateRequest();
        req.setType(PaymentType.TYPE2);
        req.setCreditorIban("LT123");
        req.setDebtorIban("LT345");
        req.setCurrency(Currency.EUR);

        assertThrows(PaymentValidationException.class,
                () -> factory.fromRequest(req, "127.0.0.1", "LT"));
    }

    @Test
    void type3RequiresCreditorBic() {
        PaymentCreateRequest req = new PaymentCreateRequest();
        req.setType(PaymentType.TYPE3);
        req.setCreditorIban("LT123");
        req.setDebtorIban("LT345");
        req.setCurrency(Currency.EUR);


        assertThrows(PaymentValidationException.class,
                () -> factory.fromRequest(req, "127.0.0.1", "LT"));
    }

    @Test
    void requiresCreditorIban() {
        PaymentCreateRequest req = new PaymentCreateRequest();
        req.setType(PaymentType.TYPE3);
        req.setDebtorIban("LT345");
        req.setCurrency(Currency.EUR);

        assertThrows(PaymentValidationException.class,
                () -> factory.fromRequest(req, "127.0.0.1", "LT"));
    }

    @Test
    void requiresDebtorIban() {
        PaymentCreateRequest req = new PaymentCreateRequest();
        req.setType(PaymentType.TYPE3);
        req.setDebtorIban("LT345");
        req.setCurrency(Currency.EUR);

        assertThrows(PaymentValidationException.class,
                () -> factory.fromRequest(req, "127.0.0.1", "LT"));
    }
}
