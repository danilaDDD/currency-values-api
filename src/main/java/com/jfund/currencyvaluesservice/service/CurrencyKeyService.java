package com.jfund.currencyvaluesservice.service;

import com.jfund.currencyvaluesservice.entity.CurrencyKey;
import com.jfund.currencyvaluesservice.repository.CurrencyKeyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CurrencyKeyService {
    private final CurrencyKeyRepository currencyKeyRepository;

    public Mono<Void> updateCurrencyKeys(Flux<CurrencyKey> currencyKeyFlux) {
        return currencyKeyFlux
                .switchIfEmpty(Flux.error(new RuntimeException("No currency keys to update")))
                .collectList()
                .flatMap(inputCurrencyKeys -> currencyKeyRepository
                        .findAll().collectList()
                        .flatMap(existCurrencyKeys -> updateCurrencyKeys(inputCurrencyKeys, existCurrencyKeys)));
    }

    private Mono<Void> updateCurrencyKeys(List<CurrencyKey> inputCurrencyKeys, List<CurrencyKey> existCurrencyKeys) {
        Set<String> inputKeys = toKeySet(inputCurrencyKeys);
        Set<String> existKeys = toKeySet(existCurrencyKeys);

        Set<String> newDifferentKeys = new HashSet<>(inputKeys);
        newDifferentKeys.removeAll(existKeys);

        Set<String> existDifferentKeys = new HashSet<>(existKeys);
        existDifferentKeys.removeAll(inputKeys);

        Mono<Void> saveAction = Flux.fromIterable(newDifferentKeys)
                .map(CurrencyKey::new)
                .flatMap(currencyKeyRepository::save)
                .then();

        Mono<Void> deleteAction = Mono.just(existDifferentKeys.stream().map(CurrencyKey::new).toList())
                .flatMap(currencyKeyRepository::deleteAll)
                .then();

        return saveAction.then(deleteAction);
    }

    private Set<String> toKeySet(List<CurrencyKey> inputCurrencyKeys) {
        return inputCurrencyKeys.stream()
                .map(CurrencyKey::getKey)
                .collect(Collectors.toSet());
    }

    public Flux<CurrencyKey> loadCurrencyKeys() {
        return currencyKeyRepository.findAll()
                .switchIfEmpty(Flux.error(new RuntimeException("No currency keys found in the database")));

    }
}
