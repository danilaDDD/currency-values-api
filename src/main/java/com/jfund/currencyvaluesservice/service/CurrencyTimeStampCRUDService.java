package com.jfund.currencyvaluesservice.service;

import com.jfund.currencyvaluesservice.entity.ChangeCurrencyValuesEvent;
import com.jfund.currencyvaluesservice.entity.CurrencyTimeStamp;
import com.jfund.currencyvaluesservice.entity.CurrencyValue;
import com.jfund.currencyvaluesservice.repository.CurrencyTimeStampRepository;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.ArrayList;

@Service
@RequiredArgsConstructor
public class CurrencyTimeStampCRUDService {
    private final CurrencyTimeStampRepository currencyTimeStampRepository;
    private final ChangeCurrencyValuesEventService changeCurrencyValuesEventService;

    public Mono<CurrencyTimeStamp> save(@NotNull CurrencyTimeStamp currencyTimeStamp) {
        return currencyTimeStampRepository
                .save(currencyTimeStamp)
                .flatMap(savedTimeStamp -> saveChangeCurrencyValuesEvent(savedTimeStamp)
                        .thenReturn(savedTimeStamp));

    }

    private Mono<ChangeCurrencyValuesEvent> saveChangeCurrencyValuesEvent(@NotNull CurrencyTimeStamp timeStamp) {
        return changeCurrencyValuesEventService
                .save(new ChangeCurrencyValuesEvent(new ArrayList<>(timeStamp.getValues()),
                        timeStamp.getDateTime(), true));
    }

    public Mono<CurrencyTimeStamp> getLastTimeStamp() {
        return currencyTimeStampRepository.findFirstByOrderByDateTimeDesc();
    }
}
