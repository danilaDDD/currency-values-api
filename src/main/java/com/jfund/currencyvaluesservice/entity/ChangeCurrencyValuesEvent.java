package com.jfund.currencyvaluesservice.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@ToString
@NoArgsConstructor
@Document
public class ChangeCurrencyValuesEvent {
    @Id
    private UUID id;
    private List<CurrencyValue> changedValues;
    private LocalDateTime changedDateTime;
    private Boolean actual;

    public ChangeCurrencyValuesEvent(List<CurrencyValue> changedValues){
        this(UUID.randomUUID(), changedValues, LocalDateTime.now(), true);
    }
}
