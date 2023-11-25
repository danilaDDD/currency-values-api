package com.jfund.currencyvaluesservice.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.AccessType;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceCreator;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.util.backoff.BackOff;

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

    private LocalDateTime dateTime = LocalDateTime.now();

    private LocalDateTime created = LocalDateTime.now();

    private LocalDateTime updated = LocalDateTime.now();

    @Field()
    private boolean sentToCandleApi = false;

    Collection<CurrencyValue> values;

    public CurrencyTimeStamp(LocalDateTime dateTime, Collection<CurrencyValue> values) {
        this.setId(dateTime.toString());
        this.setDateTime(dateTime);
        this.setValues(values);
    }
    @PersistenceCreator
    public CurrencyTimeStamp(String id, LocalDateTime dateTime, LocalDateTime created, LocalDateTime updated, Boolean sentToCandleApi, Collection<CurrencyValue> values) {
        this.setId(id);
        this.setDateTime(dateTime);
        this.setSentToCandleApi(sentToCandleApi);
        this.setUpdated(updated);
        this.setCreated(created);
        this.setValues(values);
    }

    public void setSentToCandleApi(Boolean sentToCandleApi) {
        this.sentToCandleApi = sentToCandleApi != null ? sentToCandleApi : false;
    }
}
