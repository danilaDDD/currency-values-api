package com.jfund.currencyvaluesservice.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Document
public class CurrencyKey {
    @Id
    private UUID id;
    private String key;

    public CurrencyKey(String key) {
        this(UUID.randomUUID(), key);
    }

    @Override
    public String toString() {
        return "CurrencyKey{" +
                "key='" + key + '\'' +
                '}';
    }
}
