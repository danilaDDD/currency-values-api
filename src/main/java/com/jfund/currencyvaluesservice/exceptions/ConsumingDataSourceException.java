package com.jfund.currencyvaluesservice.exceptions;

import com.fasterxml.jackson.core.JsonProcessingException;

public class ConsumingDataSourceException extends RuntimeException{
    public ConsumingDataSourceException(ConsumingDataValueNotFoundException e) {
        super(e);
    }
    public ConsumingDataSourceException(JsonProcessingException e){
        super(e);
    }
}
