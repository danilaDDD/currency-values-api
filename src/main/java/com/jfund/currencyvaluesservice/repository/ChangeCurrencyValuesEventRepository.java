package com.jfund.currencyvaluesservice.repository;

import com.jfund.currencyvaluesservice.entity.ChangeCurrencyValuesEvent;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Repository
public interface ChangeCurrencyValuesEventRepository
        extends ReactiveCrudRepository<ChangeCurrencyValuesEvent, UUID> {

    Flux<ChangeCurrencyValuesEvent> findByActualTrue();

    Mono<ChangeCurrencyValuesEvent> findFirstByOrderByChangedDateTimeDesc();
}