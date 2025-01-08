package com.jfund.currencyvaluesservice.service;

import com.jfund.currencyvaluesservice.entity.CurrencyTimeStamp;
import currencyapi.api.AsyncCurrencyApi;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

@Service
@RequiredArgsConstructor
public class LoadCurrencyTimeStampService {
    private final AsyncCurrencyApi currencyApi;

    public Flux<CurrencyTimeStamp> loadCurrencyTimeStamp(Flux<String> keysFlux) {
        return Flux.error(new RuntimeException("Not implemented"));
    }
}
