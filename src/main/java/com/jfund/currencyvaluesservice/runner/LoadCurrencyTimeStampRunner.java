package com.jfund.currencyvaluesservice.runner;

import com.jfund.currencyvaluesservice.entity.CurrencyKey;
import com.jfund.currencyvaluesservice.entity.CurrencyTimeStamp;
import com.jfund.currencyvaluesservice.service.CurrencyKeyService;
import com.jfund.currencyvaluesservice.service.LoadCurrencyTimeStampService;
import com.jfund.currencyvaluesservice.service.SaveCurrencyTimeStampService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Slf4j
@Component
@RequiredArgsConstructor
public class LoadCurrencyTimeStampRunner implements Runnable{
    private final CurrencyKeyService currencyKeyService;
    private final LoadCurrencyTimeStampService loadCurrencyTimeStampService;
    private final SaveCurrencyTimeStampService saveCurrencyTimeStampService;

    @Override
    public void run() {
        Mono<CurrencyTimeStamp> loadedTimeStampMono = loadCurrencyTimeStampService
                 .loadCurrencyTimeStamp(currencyKeyService.loadCurrencyKeys().map(CurrencyKey::getKey));

        saveCurrencyTimeStampService.saveCurrencyTimeStampIfDifferent(loadedTimeStampMono)
                .doOnError(e -> log.error(e.getMessage()))
                .subscribe();

    }
}
