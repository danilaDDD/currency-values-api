package com.jfund.currencyvaluesservice.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceCreator;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;
@Accessors(chain = true)
@Getter
@Setter
@Document
public class ChangeCurrencyValuesEntity {
    @Id
    private String id;
    private List<CurrencyValue> changedValues;

    private LocalDateTime changedDateTime;

    @PersistenceCreator
    public ChangeCurrencyValuesEntity(String id, List<CurrencyValue> changedValues, LocalDateTime changedDateTime){
        setId(id);
        setChangedValues(changedValues);
        setChangedDateTime(changedDateTime);
    }

    public ChangeCurrencyValuesEntity(List<CurrencyValue> changedValues, LocalDateTime changedDateTime) {
        setChangedDateTime(changedDateTime);
        setChangedValues(changedValues);
    }
}
