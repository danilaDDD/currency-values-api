package com.jfund.currencyvaluesservice.entity;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDateTime;
@Getter
@Document(collation = "stringProperties")
public class KeyStringValueEntity {
    @Setter
    @Id
    private String key;

    @Setter
    @Field
    private String stringValue;

    @Field
    LocalDateTime updated = LocalDateTime.now();

    @Field
    LocalDateTime created = LocalDateTime.now();

    public KeyStringValueEntity(String key, String stringValue) {
        this.key = key;
        this.stringValue = stringValue;
    }
    public void onUpdate(){
        updated = LocalDateTime.now();
    }
}
