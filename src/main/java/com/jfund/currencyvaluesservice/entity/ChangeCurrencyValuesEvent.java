package com.jfund.currencyvaluesservice.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Getter
@Setter
@ToString
@NoArgsConstructor
@Document
public class ChangeCurrencyValuesEvent {
    @Id
    private String id;
    private List<CurrencyValue> changedValues;
    private LocalDateTime changedDateTime;
    private Boolean actual;

    public ChangeCurrencyValuesEvent(List<CurrencyValue> changedValues, LocalDateTime changedDateTime, Boolean actual) {
        this.changedValues = changedValues;
        this.changedDateTime = changedDateTime;
        this.actual = actual;
    }

    public ChangeCurrencyValuesEvent(List<CurrencyValue> changedValues, LocalDateTime changedDateTime) {
        this(changedValues, changedDateTime, true);
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        ChangeCurrencyValuesEvent that = (ChangeCurrencyValuesEvent) object;

        if(id != null)
            return Objects.equals(id, that.id);
        else
            return Objects.equals(changedDateTime, that.changedDateTime);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id != null ? id: changedDateTime);
    }
}
