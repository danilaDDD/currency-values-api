package com.jfund.currencyvaluesservice.service;

import com.jfund.currencyvaluesservice.entity.CurrencyTimeStamp;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class SaveCurrencyTimeStampService {
    public boolean saveCurrencyTimeStampIfDifferent(CurrencyTimeStamp timeStamp) {
        throw new RuntimeException("Not implemented");
    }
}
