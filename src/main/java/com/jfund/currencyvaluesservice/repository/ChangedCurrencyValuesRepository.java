package com.jfund.currencyvaluesservice.repository;

import com.jfund.currencyvaluesservice.entity.ChangeCurrencyValuesEvent;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChangedCurrencyValuesRepository extends MongoRepository<ChangeCurrencyValuesEvent, String> {}
