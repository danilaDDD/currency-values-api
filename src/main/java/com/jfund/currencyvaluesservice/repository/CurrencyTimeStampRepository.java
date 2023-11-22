package com.jfund.currencyvaluesservice.repository;

import com.jfund.currencyvaluesservice.entity.CurrencyTimeStamp;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CurrencyTimeStampRepository extends MongoRepository<CurrencyTimeStamp, String> {
    List<CurrencyTimeStamp> findByOrderByDateTimeDesc(Pageable pageable);
}
