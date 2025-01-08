package com.jfund.currencyvaluesservice.service;

import com.jfund.currencyvaluesservice.entity.CurrencyKey;
import com.jfund.currencyvaluesservice.repository.CurrencyKeyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class CurrencyKeyCRUDService {
    private final CurrencyKeyRepository currencyKeyRepository;

    public Mono<Void> updateCurrencyKeys(Flux<CurrencyKey> currencyKeyFlux) {
        return Mono.error(new RuntimeException("Not implemented"));
    }

    public Flux<CurrencyKey> loadCurrencyKeys() {
        return currencyKeyRepository.findAll()
                .switchIfEmpty(Flux.error(new RuntimeException("No currency keys found in the database")));

    }
}
