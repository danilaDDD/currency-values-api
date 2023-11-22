package com.jfund.currencyvaluesservice.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.AccessType;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceCreator;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Map;

@Getter
@Setter
@Document(collection = "timeStamps")
public class CurrencyTimeStamp {
    @Id
    private String id = LocalDateTime.now().toString();

    @Field
    private LocalDateTime dateTime = LocalDateTime.now();

    @Field
    private LocalDateTime created = LocalDateTime.now();

    @Field
    private LocalDateTime updated = LocalDateTime.now();

    Collection<CurrencyValue> values;

    public CurrencyTimeStamp(LocalDateTime dateTime, Collection<CurrencyValue> values) {
        this.setId(dateTime.toString());
        this.setDateTime(dateTime);
        this.setValues(values);
    }
    @PersistenceCreator
    public CurrencyTimeStamp(String id, LocalDateTime dateTime, LocalDateTime updated, LocalDateTime created, Collection<CurrencyValue> values) {
        this.id = id;
        this.dateTime = dateTime;
        this.updated = updated;
        this.created = created;
        this.values = values;
    }
}
