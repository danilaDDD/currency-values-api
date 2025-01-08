package com.jfund.currencyvaluesservice.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jfund.currencyvaluesservice.data.SerializableChangeCurrencyValues;
import com.jfund.currencyvaluesservice.exceptions.SendChangeValuesException;
import com.jfund.currencyvaluesservice.repository.ChangeCurrencyValuesEventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

@Service
@RequiredArgsConstructor
public class ChangeCurrencyValuesEventService {
    private final ChangeCurrencyValuesEventRepository eventRepository;
    private final ObjectMapper objectMapper;

    public Flux<String> getSerializedFlux() {
        return getChangeCurrencyValuesEvents()
                .handle((serializedEvent, sink) -> {
                    try {
                        sink.next(objectMapper.writeValueAsString(serializedEvent));
                    } catch (JsonProcessingException e) {
                        sink.error(new SendChangeValuesException(e));
                    }
                });
    }

    private Flux<SerializableChangeCurrencyValues> getChangeCurrencyValuesEvents() {
            return Flux.error(new RuntimeException("Not implemented"));
    }
}
