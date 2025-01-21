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
        Mono<CurrencyTimeStamp> lastTimeStampMono = repository
                .findFirstByOrderByDateTimeDesc();

        return Mono.zip(lastTimeStampMono, inputTimeStampMono)
                .flatMap(t -> performAction(t.getT1(), t.getT2()));

    }

    private Mono<CurrencyTimeStamp> performAction(CurrencyTimeStamp lastTimeStamp,
                                                  CurrencyTimeStamp inputTimeStamp) {

        if(inputTimeStamp.getValues().isEmpty())
            return Mono.error(new SaveNewValuesException("input values list is empty"));

        if(lastTimeStamp.getDateTime().equals(inputTimeStamp.getDateTime())){
            return Mono.empty();
        }

        Set<CurrencyValue> differentValuesSet = new TreeSet<>(CURRENCY_VALUE_COMPARATOR);
        Set<CurrencyValue> lastValuesSet = new TreeSet<>(CURRENCY_VALUE_COMPARATOR);
        lastValuesSet.addAll(lastTimeStamp.getValues());
        differentValuesSet.addAll(inputTimeStamp.getValues());

        differentValuesSet.removeAll(lastValuesSet);
        if(differentValuesSet.isEmpty()){
            return Mono.empty();
        }

        lastValuesSet.addAll(differentValuesSet);
        CurrencyTimeStamp newTimeStamp = new CurrencyTimeStamp(inputTimeStamp.getDateTime(), differentValuesSet);
        return repository.save(newTimeStamp);
    }
}
