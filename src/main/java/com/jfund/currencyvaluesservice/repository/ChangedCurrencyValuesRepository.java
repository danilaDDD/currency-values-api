package com.jfund.currencyvaluesservice.repository;

import com.jfund.currencyvaluesservice.entity.ChangeCurrencyValuesEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface ChangedCurrencyValuesRepository extends MongoRepository<ChangeCurrencyValuesEntity, String> {}
