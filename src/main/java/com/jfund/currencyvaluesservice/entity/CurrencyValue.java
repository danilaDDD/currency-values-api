package com.jfund.currencyvaluesservice.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CurrencyValue {
    private String key;
    private Float value;

}
