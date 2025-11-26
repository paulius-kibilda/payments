package com.payments.service;

import com.payments.external.IpCountryClient;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ClientCountryService {

    private static final Logger log = LoggerFactory.getLogger(ClientCountryService.class);

    private final IpCountryClient ipCountryClient;

    public String resolveAndLog(String ip) {
        String country = ipCountryClient.resolveCountry(ip);
        log.info("Client IP={} resolved country={}", ip, country);
        return country;
    }
}
