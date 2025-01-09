package com.jfund.currencyvaluesservice.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

@Data
@AllArgsConstructor
@ToString
public class CurrencyValue {
    private String key;
    private Float value;
}
