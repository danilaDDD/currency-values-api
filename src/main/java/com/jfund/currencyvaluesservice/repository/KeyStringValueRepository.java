package com.jfund.currencyvaluesservice.repository;

import com.jfund.currencyvaluesservice.entity.KeyStringValueEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface KeyStringValueRepository extends MongoRepository<KeyStringValueEntity, String> {
}
