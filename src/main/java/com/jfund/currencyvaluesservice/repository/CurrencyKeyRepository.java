package com.jfund.currencyvaluesservice.repository;

import com.jfund.currencyvaluesservice.entity.CurrencyKey;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface CurrencyKeyRepository extends ReactiveMongoRepository<CurrencyKey, String> {
}
