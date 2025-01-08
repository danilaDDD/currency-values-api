package com.jfund.currencyvaluesservice.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@Document
public class ChangeCurrencyValuesEvent {
    @Id
    private UUID id;
    private List<CurrencyValue> changedValues;
    private LocalDateTime changedDateTime;

    public ChangeCurrencyValuesEvent(List<CurrencyValue> changedValues){
        this(UUID.randomUUID(), changedValues, LocalDateTime.now());
    }
}
