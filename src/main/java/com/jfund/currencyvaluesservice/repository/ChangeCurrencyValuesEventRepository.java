package com.jfund.currencyvaluesservice.repository;

import com.jfund.currencyvaluesservice.entity.ChangeCurrencyValuesEvent;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

import java.util.UUID;

public interface ChangeCurrencyValuesEventRepository extends ReactiveCrudRepository<ChangeCurrencyValuesEvent, UUID> {
}