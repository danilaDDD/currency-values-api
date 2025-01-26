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
@NoArgsConstructor
@ToString
@Document
public class CurrencyTimeStamp {
    @Id
    private String id;
    private LocalDateTime dateTime;
    private Collection<CurrencyValue> values;
    private boolean sent;

    public CurrencyTimeStamp(LocalDateTime dateTime, Collection<CurrencyValue> values, boolean sent) {
        this.dateTime = dateTime;
        this.values = values;
        this.sent = sent;
    }

    public CurrencyTimeStamp(LocalDateTime dateTime, Collection<CurrencyValue> values) {
       this(dateTime, values, false);
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        CurrencyTimeStamp that = (CurrencyTimeStamp) object;
        if(id != null)
            return Objects.equals(id, that.id);
        else
            return Objects.equals(dateTime, that.dateTime);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id != null ? id: dateTime);
    }
}
