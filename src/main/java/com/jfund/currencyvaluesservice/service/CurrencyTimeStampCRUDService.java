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
import java.util.List;

@Service
@RequiredArgsConstructor
public class CurrencyTimeStampCRUDService {
    private final CurrencyTimeStampRepository currencyTimeStampRepository;
    private final ChangeCurrencyValuesEventService changeCurrencyValuesEventService;

    public Mono<Void> save(@NotNull CurrencyTimeStamp currencyTimeStamp) {
        /**
         * return Mono of action of saving currencyTimeStamp to repository then saving the event
         */
        return currencyTimeStampRepository.save(currencyTimeStamp)
                .then(saveChangeCurrencyValuesEvent(currencyTimeStamp));


    }

    private Mono<Void> saveChangeCurrencyValuesEvent(CurrencyTimeStamp timeStamp) {
        return changeCurrencyValuesEventService
                .save(new ArrayList<>(timeStamp.getValues()));
    }

    public Mono<CurrencyTimeStamp> getLastTimeStamp() {
        return currencyTimeStampRepository.findFirstByOrderByDateTimeDesc();
    }
}
