package com.jfund.currencyvaluesservice.exceptions;

import com.jfund.currencyvaluesservice.entity.ChangeCurrencyValuesEvent;

public class SendChangeValuesException extends RuntimeException {
    public SendChangeValuesException(Exception e) {
        super(e);
    }

    public SendChangeValuesException(ChangeCurrencyValuesEvent changeEvent) {
        super(String.format("serialize %s with exception", changeEvent));
    }
}
