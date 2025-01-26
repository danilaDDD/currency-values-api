package com.jfund.currencyvaluesservice.data;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@AllArgsConstructor
public class SerializableCurrencyTimeStamp {
    private final UUID id;
    private final String serializedValues;
    private final LocalDateTime changedDateTime;

    public SerializableCurrencyTimeStamp(String serializedValues, LocalDateTime changedDateTime) {
        this(UUID.randomUUID(), serializedValues, changedDateTime);
    }
}
