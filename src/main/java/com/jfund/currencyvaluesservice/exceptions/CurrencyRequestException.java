package com.jfund.currencyvaluesservice.exceptions;

import currencyapi.exceptions.CurrencyBadRequestException;

import java.util.concurrent.ExecutionException;
public class CurrencyRequestException extends RuntimeException{
    public CurrencyRequestException(CurrencyBadRequestException e) {
        super(e);
    }
    public CurrencyRequestException(ExecutionException e) {
        super(e);
    }
    public CurrencyRequestException(InterruptedException e) {
        super(e);
    }
}
