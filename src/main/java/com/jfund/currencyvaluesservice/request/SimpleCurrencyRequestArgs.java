package com.jfund.currencyvaluesservice.request;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

@Accessors(chain = true)
@Data
public class SimpleCurrencyRequestArgs implements CurrencyRequestArgs{
    private List<String> currencyKeys;
}
