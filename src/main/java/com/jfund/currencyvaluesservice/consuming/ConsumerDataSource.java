package com.jfund.currencyvaluesservice.consuming;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jfund.currencyvaluesservice.exceptions.ConsumingDataValueNotFoundException;
import com.jfund.currencyvaluesservice.exceptions.ConsumingDataSourceException;
import com.jfund.currencyvaluesservice.service.KeyStringValueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
@Component
public class ConsumerDataSource {
    private final String currencyDataKey = ConsumerSettings.CURRENCY_KEYS_KEY;
    private KeyStringValueService keyStringValueService;

    @Autowired
    public void setKeyStringValueService(KeyStringValueService keyStringValueService) {
        this.keyStringValueService = keyStringValueService;
    }

    public List<String> getCurrencyKeys() {
        try {
            String serializedCurrencyKeys = keyStringValueService.getStringValue(currencyDataKey);
            return new ObjectMapper().readValue(serializedCurrencyKeys, new TypeReference<>() {
            });

        } catch (JsonProcessingException e) {
            throw new ConsumingDataSourceException(e);

        } catch (ConsumingDataValueNotFoundException e) {
            throw new ConsumingDataSourceException(e);
        }
    }
}
