package com.jfund.currencyvaluesservice.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.UUID;

@Data
@AllArgsConstructor
@Document(collation = "currency_keys")
public class CurrencyKey {
    @Id
    private UUID id;
    private String key;

    public CurrencyKey(String key) {
        this(UUID.randomUUID(), key);
    }
}
