package com.jfund.currencyvaluesservice.runner;

import com.jfund.currencyvaluesservice.entity.CurrencyKey;
import com.jfund.currencyvaluesservice.entity.CurrencyTimeStamp;
import com.jfund.currencyvaluesservice.service.CurrencyKeyCRUDService;
import com.jfund.currencyvaluesservice.service.LoadCurrencyTimeStampService;
import com.jfund.currencyvaluesservice.service.SaveCurrencyTimeStampService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class LoadCurrencyTimeStampRunner implements Runnable{
    private final CurrencyKeyCRUDService currencyKeyCRUDService;
    private final LoadCurrencyTimeStampService loadCurrencyTimeStampService;
    private final SaveCurrencyTimeStampService saveCurrencyTimeStampService;

    @Override
    public void run() {
        Mono<CurrencyTimeStamp> loadedTimeStampMono = loadCurrencyTimeStampService
                 .loadCurrencyTimeStamp(currencyKeyCRUDService.loadCurrencyKeys().map(CurrencyKey::getKey));

        loadedTimeStampMono.subscribe(saveCurrencyTimeStampService::saveCurrencyTimeStampIfDifferent);

    }
}
