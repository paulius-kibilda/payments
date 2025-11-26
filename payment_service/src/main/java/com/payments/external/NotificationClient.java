package com.payments.external;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class NotificationClient {

    private final WebClient notificationWebClient;

    public boolean notify(String url) {
        return sendGet(url);
    }

    private boolean sendGet(String url) {
        try {
            HttpStatusCode status = notificationWebClient
                    .get()
                    .uri(url)
                    .retrieve()
                    .toBodilessEntity()
                    .map(response -> response.getStatusCode())
                    .onErrorResume(ex -> Mono.empty())
                    .block();

            return status != null && status.is2xxSuccessful();

        } catch (Exception e) {
            return false;
        }
    }
}
