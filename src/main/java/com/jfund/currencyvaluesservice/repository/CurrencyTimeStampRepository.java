package com.jfund.currencyvaluesservice.repository;

import com.jfund.currencyvaluesservice.entity.CurrencyTimeStamp;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

import java.util.UUID;

public interface CurrencyTimeStampRepository
        extends ReactiveCrudRepository<CurrencyTimeStamp, UUID> {

}