package com.jfund.currencyvaluesservice.service;

import com.jfund.currencyvaluesservice.entity.CurrencyTimeStamp;
import com.jfund.currencyvaluesservice.entity.CurrencyValue;
import com.jfund.currencyvaluesservice.exceptions.SaveNewValuesException;
import com.jfund.currencyvaluesservice.repository.CurrencyTimeStampRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.*;

@Service
@RequiredArgsConstructor
public class SaveCurrencyTimeStampService {
    private final static Comparator<CurrencyValue> CURRENCY_VALUE_COMPARATOR = Comparator.comparing(CurrencyValue::getValue);
    private final CurrencyTimeStampRepository repository;

    public Mono<CurrencyTimeStamp> saveCurrencyTimeStampIfDifferent(Mono<CurrencyTimeStamp> inputTimeStampMono) {

        return repository
                .findFirstByOrderByDateTimeDesc()
                .flatMap(lastTimeStamp -> inputTimeStampMono
                        .flatMap(inputTimeStamp -> performAction(lastTimeStamp, inputTimeStamp)))
                .switchIfEmpty(inputTimeStampMono
                        .flatMap(inputTimeStamp -> performAction(null, inputTimeStamp)))
                .onErrorResume(NotSavedTimeStampException.class, e -> Mono.empty());



    }

    private Mono<CurrencyTimeStamp> performAction(CurrencyTimeStamp lastTimeStamp,
                                                  CurrencyTimeStamp inputTimeStamp) {
        if(lastTimeStamp == null)
            return repository.save(inputTimeStamp);

        if(inputTimeStamp.getValues().isEmpty())
            return Mono.error(new SaveNewValuesException("input values list is empty"));

        if(lastTimeStamp.getDateTime().equals(inputTimeStamp.getDateTime())){
            return Mono.error(new NotSavedTimeStampException("input timestamp date time is the same as last timestamp"));
        }

        if(inputTimeStamp.getDateTime().isBefore(lastTimeStamp.getDateTime())){
            return Mono.error(new SaveNewValuesException("input timestamp date time is after last timestamp"));
        }

        Set<CurrencyValue> differentValuesSet = new TreeSet<>(CURRENCY_VALUE_COMPARATOR);
        Set<CurrencyValue> lastValuesSet = new TreeSet<>(CURRENCY_VALUE_COMPARATOR);
        lastValuesSet.addAll(lastTimeStamp.getValues());
        differentValuesSet.addAll(inputTimeStamp.getValues());

        differentValuesSet.removeAll(lastValuesSet);
        if(differentValuesSet.isEmpty()){
            return Mono.error(new NotSavedTimeStampException("input timestamp has the same values as last timestamp"));
        }

        CurrencyTimeStamp newTimeStamp = new CurrencyTimeStamp(inputTimeStamp.getDateTime(), differentValuesSet);
        return repository.save(newTimeStamp);
    }
}

class NotSavedTimeStampException extends RuntimeException {
    public NotSavedTimeStampException(String message) {
        super(message);
    }
}
