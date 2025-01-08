package com.jfund.currencyvaluesservice.service;

import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class ConsumingCurrencyKeysService {
    public Mono<Void> upgradeCurrencyKeys(String serializedKeysValueObject) {
        return Mono.error(new RuntimeException("Not implemented"));
    }
}
