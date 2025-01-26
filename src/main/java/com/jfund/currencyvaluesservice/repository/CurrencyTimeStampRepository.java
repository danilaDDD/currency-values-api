package com.jfund.currencyvaluesservice.repository;

import com.jfund.currencyvaluesservice.entity.CurrencyTimeStamp;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Repository
public interface CurrencyTimeStampRepository
        extends ReactiveCrudRepository<CurrencyTimeStamp, UUID> {
    Mono<CurrencyTimeStamp> findFirstByOrderByDateTimeDesc();

    Flux<CurrencyTimeStamp> findByOrderByDateTimeDesc();

    Flux<CurrencyTimeStamp> findBySentFalseOrderByDateTimeDesc();
}