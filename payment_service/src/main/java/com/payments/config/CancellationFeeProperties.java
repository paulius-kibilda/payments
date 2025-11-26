package com.payments.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "cancellation")
public class CancellationFeeProperties {

    private BigDecimal type1Fee;

    private BigDecimal type2Fee;

    private BigDecimal type3Fee;

}
