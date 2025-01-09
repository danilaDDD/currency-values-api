package com.jfund.currencyvaluesservice.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Objects;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@ToString
@Document
public class CurrencyKey {
    @Id
    private String id;
    private String key;

    public CurrencyKey(String key) {
        this.key = key;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        CurrencyKey that = (CurrencyKey) object;

        if(id != null)
            return Objects.equals(id, that.id);
        else
            return Objects.equals(key, that.key);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id != null ? id: key);
    }
}
