package com.jfund.currencyvaluesservice.exceptions;

import com.jfund.currencyvaluesservice.entity.CurrencyTimeStamp;

public class SendChangeValuesException extends RuntimeException {
    public SendChangeValuesException(Exception e) {
        super(e);
    }

    public SendChangeValuesException(CurrencyTimeStamp currencyTimeStamp) {
        super(String.format("serialize %s with exception", currencyTimeStamp));
    }
}
