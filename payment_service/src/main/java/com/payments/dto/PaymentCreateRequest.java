package com.payments.dto;

import com.payments.domain.model.Currency;
import com.payments.domain.model.PaymentType;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PaymentCreateRequest {

    @NotNull
    private PaymentType type;

    @NotNull
    @DecimalMin(value = "0.01")
    private BigDecimal amount;

    @NotNull
    private Currency currency;

    @NotBlank
    private String debtorIban;

    @NotBlank
    private String creditorIban;

    private String details;

    private String creditorBic;

}
