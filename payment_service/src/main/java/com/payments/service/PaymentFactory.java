package com.payments.service;

import com.payments.domain.model.Currency;
import com.payments.domain.model.Payment;
import com.payments.domain.model.PaymentType;
import com.payments.dto.PaymentCreateRequest;
import com.payments.service.exception.PaymentCancellationException;
import com.payments.service.exception.PaymentValidationException;
import org.apache.commons.lang3.EnumUtils;
import org.springframework.stereotype.Component;

@Component
public class PaymentFactory {

    public Payment fromRequest(PaymentCreateRequest req, String clientIp, String clientCountry) {
        validateTypeSpecific(req);

        Payment p = new Payment();
        p.setType(req.getType());
        p.setAmount(req.getAmount());
        p.setCurrency(req.getCurrency());
        p.setDebtorIban(req.getDebtorIban());
        p.setCreditorIban(req.getCreditorIban());
        p.setDetails(req.getDetails());
        p.setCreditorBic(req.getCreditorBic());
        p.setClientIp(clientIp);
        p.setClientCountry(clientCountry);
        p.setCanceled(false);
        return p;
    }

    private void validateTypeSpecific(PaymentCreateRequest req) {
        PaymentType type = req.getType();
        Currency currency = req.getCurrency();

        if(req.getCreditorIban() == null || req.getCreditorIban().isEmpty()) {
            throw new PaymentValidationException("Creditor Iban is required");
        }

        if(req.getDebtorIban() == null || req.getDebtorIban().isEmpty()) {
            throw new PaymentValidationException("Debtor Iban is required");
        }

        if(req.getDebtorIban().equals(req.getCreditorIban())) {
            throw new PaymentValidationException("Creditor Iban is the same as Debtor Iban");
        }

        switch (type) {
            case TYPE1 -> {
                if (currency != Currency.EUR) {
                    throw new PaymentValidationException("TYPE1 is only applicable for EUR payments");
                }
                if (req.getDetails() == null || req.getDetails().isBlank()) {
                    throw new PaymentValidationException("TYPE1 requires details");
                }
            }
            case TYPE2 -> {
                if (currency != Currency.USD) {
                    throw new PaymentValidationException("TYPE2 is only applicable for USD payments");
                }
            }
            case TYPE3 -> {
                if (req.getCreditorBic() == null || req.getCreditorBic().isBlank()) {
                    throw new PaymentValidationException("TYPE3 requires creditor BIC");
                }
            }
        }
    }
}
