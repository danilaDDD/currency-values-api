package com.jfund.currencyvaluesservice.entity;

import lombok.*;
import org.springframework.data.annotation.AccessType;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceCreator;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.util.backoff.BackOff;

import java.time.LocalDateTime;
import java.util.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Document
public class CurrencyTimeStamp {
    @Id
    private UUID id;
    private LocalDateTime dateTime;
    private Collection<CurrencyValue> values;
    private Boolean sentToCandleApi;

    public CurrencyTimeStamp(LocalDateTime dateTime, Collection<CurrencyValue> values, Boolean sentToCandleApi) {
        this(UUID.randomUUID(), dateTime, values.stream().toList(), sentToCandleApi);
    }

    public CurrencyTimeStamp(LocalDateTime dateTime, Collection<CurrencyValue> values) {
       this(dateTime, values, true);
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        CurrencyTimeStamp that = (CurrencyTimeStamp) object;

        return Objects.equals(dateTime, that.dateTime);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(dateTime);
    }
}
