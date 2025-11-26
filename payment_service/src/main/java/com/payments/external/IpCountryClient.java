package com.payments.external;

import com.payments.dto.IpCountryResponse;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class IpCountryClient {

    private static final Logger log = LoggerFactory.getLogger(IpCountryClient.class);

    private final WebClient ipCountryWebClient;

    public String resolveCountry(String ip) {
        if (ip == null || ip.isBlank()) {
            return null;
        }
        try {
            return ipCountryWebClient.get()
                    .uri("http://ip-api.com/json/" + ip + "?fields=country")
                    .retrieve()
                    .bodyToMono(IpCountryResponse.class)
                    .map(IpCountryResponse::getCountryCode)
                    .onErrorResume(ex -> {
                        log.warn("Failed to resolve country for IP {}: {}", ip, ex.getMessage());
                        return Mono.empty();
                    })
                    .block();
        } catch (Exception e) {
            log.warn("Failed to resolve country for IP {}: {}", ip, e.getMessage());
            return null;
        }
    }
}
