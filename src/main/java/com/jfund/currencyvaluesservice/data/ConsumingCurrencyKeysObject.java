package com.jfund.currencyvaluesservice.data;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ConsumingCurrencyKeysObject {
    private String serializedData;
    private boolean empty;
    private LocalDateTime createdAt;
}
