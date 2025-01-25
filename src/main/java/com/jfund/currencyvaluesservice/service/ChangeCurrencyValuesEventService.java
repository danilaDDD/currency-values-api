package com.jfund.currencyvaluesservice.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jfund.currencyvaluesservice.data.SerializableChangeCurrencyValues;
import com.jfund.currencyvaluesservice.entity.ChangeCurrencyValuesEvent;
import com.jfund.currencyvaluesservice.entity.CurrencyValue;
import com.jfund.currencyvaluesservice.exceptions.SendChangeValuesException;
import com.jfund.currencyvaluesservice.repository.ChangeCurrencyValuesEventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ChangeCurrencyValuesEventService {
    private final ChangeCurrencyValuesEventRepository eventRepository;
    private final ObjectMapper objectMapper;

    public Flux<String> getSerializedFlux() {
        return getChangeCurrencyValuesEvents();
    }

    private Flux<String> getChangeCurrencyValuesEvents() {
            Flux<ChangeCurrencyValuesEvent> changeEventsFlux = eventRepository.findByActualTrue();
            return changeEventsFlux.map(this::serialize);

    }

    private String serialize(ChangeCurrencyValuesEvent changeEvent) {
        try {
            String serializedValues = objectMapper.writeValueAsString(changeEvent);
            SerializableChangeCurrencyValues serializedEvent =
                    new SerializableChangeCurrencyValues(serializedValues, changeEvent.getChangedDateTime());
            String stringResult = objectMapper.writeValueAsString(serializedEvent);

            changeEvent.setActual(false);
            eventRepository.save(changeEvent).subscribe();

            return stringResult;
        }catch(JsonProcessingException e){
            throw new SendChangeValuesException(changeEvent);
        }
    }

    public Mono<ChangeCurrencyValuesEvent> save(ChangeCurrencyValuesEvent event) {
        return eventRepository.save(event);
    }
}
