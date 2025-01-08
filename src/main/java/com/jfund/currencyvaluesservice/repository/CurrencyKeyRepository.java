package com.jfund.currencyvaluesservice.repository;

import com.jfund.currencyvaluesservice.entity.CurrencyKey;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

import java.util.UUID;

public interface CurrencyKeyRepository extends ReactiveMongoRepository<CurrencyKey, UUID> {
}
