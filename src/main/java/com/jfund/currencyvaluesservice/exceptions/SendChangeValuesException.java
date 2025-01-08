package com.jfund.currencyvaluesservice.exceptions;

public class SendChangeValuesException extends RuntimeException {
    public SendChangeValuesException(Exception e) {
        super(e);
    }
}
