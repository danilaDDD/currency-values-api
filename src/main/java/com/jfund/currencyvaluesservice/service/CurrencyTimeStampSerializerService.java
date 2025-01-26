package com.jfund.currencyvaluesservice.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jfund.currencyvaluesservice.data.SerializableCurrencyTimeStamp;
import com.jfund.currencyvaluesservice.entity.CurrencyTimeStamp;
import com.jfund.currencyvaluesservice.exceptions.SendChangeValuesException;
import com.jfund.currencyvaluesservice.repository.CurrencyTimeStampRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.util.HashSet;

@Service
@RequiredArgsConstructor
public class CurrencyTimeStampSerializerService {
    private final CurrencyTimeStampRepository currencyTimeStampRepository;
    private final ObjectMapper objectMapper;

    public Flux<String> getSerializedFlux() {
        return getChangeCurrencyValuesEvents();
    }

    private Flux<String> getChangeCurrencyValuesEvents() {
            return currencyTimeStampRepository
                    .findBySentFalseOrderByDateTimeDesc()
                    .flatMap(this::serialize);


    }

    private Flux<String> serialize(CurrencyTimeStamp timeStamp) {
        try {
            if(timeStamp.getValues().isEmpty()){
                return Flux.error(new SendChangeValuesException(timeStamp));
            }
            // copy by HaseSet for order of values
            String serializedValues = objectMapper.writeValueAsString(new HashSet<>(timeStamp.getValues()));
            SerializableCurrencyTimeStamp serializedEvent =
                    new SerializableCurrencyTimeStamp(serializedValues, timeStamp.getDateTime());
            String stringResult = objectMapper.writeValueAsString(serializedEvent);

            timeStamp.setSent(true);
            return currencyTimeStampRepository
                    .save(timeStamp)
                    .thenReturn(stringResult)
                    .flux();

        }catch(JsonProcessingException e){
            return Flux.error(new SendChangeValuesException(timeStamp));
        }
    }
}
