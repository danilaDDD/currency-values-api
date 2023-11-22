package com.jfund.currencyvaluesservice.service;

import com.jfund.currencyvaluesservice.entity.CurrencyTimeStamp;
import com.jfund.currencyvaluesservice.entity.CurrencyValue;
import com.jfund.currencyvaluesservice.repository.CurrencyTimeStampRepository;
import com.jfund.currencyvaluesservice.testutils.CustomerTestUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

@DataMongoTest
public class CurrencyTimeStampRepositoryTest {

    private CurrencyTimeStampRepository currencyTimeStampRepository;

    @Autowired
    public void setCurrencyTimeStampRepository(CurrencyTimeStampRepository currencyTimeStampRepository) {
        this.currencyTimeStampRepository = currencyTimeStampRepository;
    }
    @Test
    public void shouldSuccessSaveEntity() {
        Collection<CurrencyValue> currencyValues = List.of(
                new CurrencyValue("USDRUB", 100F),
                new CurrencyValue("EURRUB", 98.12F)
        );

        CurrencyTimeStamp timeStamp = new CurrencyTimeStamp(LocalDateTime.now(), currencyValues);

        currencyTimeStampRepository.save(timeStamp);
    }
}

