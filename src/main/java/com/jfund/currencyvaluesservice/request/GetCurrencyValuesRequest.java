package com.jfund.currencyvaluesservice.request;

import org.springframework.stereotype.Component;

import java.util.Map;
public interface GetCurrencyValuesRequest {
    Map<String, Float> getResponse(CurrencyRequestArgs args);
}
