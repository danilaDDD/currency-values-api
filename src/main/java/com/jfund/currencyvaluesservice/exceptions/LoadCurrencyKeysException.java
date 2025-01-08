package com.jfund.currencyvaluesservice.exceptions;

public class LoadCurrencyKeysException extends RuntimeException{
    public LoadCurrencyKeysException(Exception e) {
        super(e);
    }

    public LoadCurrencyKeysException(String message) {
        super(message);
    }
}
