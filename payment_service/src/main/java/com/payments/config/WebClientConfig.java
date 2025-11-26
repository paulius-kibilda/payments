package com.payments.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

    @Bean
    public WebClient notificationWebClient() {
        return WebClient.builder().build();
    }

    @Bean
    public WebClient ipCountryWebClient() {
        return WebClient.builder().build();
    }
}
