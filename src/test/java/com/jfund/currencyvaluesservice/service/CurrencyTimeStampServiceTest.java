package com.jfund.currencyvaluesservice.service;

import com.jfund.currencyvaluesservice.entity.CurrencyTimeStamp;
import com.jfund.currencyvaluesservice.entity.CurrencyValue;
import com.jfund.currencyvaluesservice.testutils.CustomerTestUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

@SpringBootTest
public class CurrencyTimeStampServiceTest {
    private CurrencyTimeStampService currencyTimeStampService;
    private CustomerTestUtils customerTestUtils;

    @Autowired
    public void setCurrencyTimeStampService(CurrencyTimeStampService currencyTimeStampService) {
        this.currencyTimeStampService = currencyTimeStampService;
    }
    @Autowired
    public void setCustomerTestUtils(CustomerTestUtils customerTestUtils) {
        this.customerTestUtils = customerTestUtils;
    }

    @BeforeEach
    public void beforeEach(){
        customerTestUtils.dropCurrencyValuesTable();
    }

    @Test
    public void shouldSuccessSAveEntity(){
        Collection<CurrencyValue> currencyValues = List.of(
                new CurrencyValue("USDRUB", 100F),
                new CurrencyValue("EURRUB", 98.12F)
        );

        CurrencyTimeStamp timeStamp = new CurrencyTimeStamp(LocalDateTime.now(), currencyValues);
        currencyTimeStampService.saveEntity(timeStamp);
    }
}
