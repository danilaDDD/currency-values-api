package com.jfund.currencyvaluesservice.consuming;

import com.jfund.currencyvaluesservice.saver.CurrencyKeysValueSaver;
import com.jfund.jfundclilib.UpdateOrCreateData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class KafkaListeners {
    private CurrencyKeysValueSaver currencyKeysValueSaver;
    @Autowired
    public void setCurrencyKeysValueSaver(CurrencyKeysValueSaver currencyKeysValueSaver) {
        this.currencyKeysValueSaver = currencyKeysValueSaver;
    }

    @KafkaListener(topics = "currency-keys-topic", groupId = "currency-keys-group")
    public UpdateOrCreateData listenAvailableCurrencyKeys(String currencyKeysValue){
        System.out.println(currencyKeysValue);
        return currencyKeysValueSaver.save(currencyKeysValue);
    }
}
