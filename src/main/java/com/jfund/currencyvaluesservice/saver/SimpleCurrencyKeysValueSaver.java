package com.jfund.currencyvaluesservice.saver;

import com.jfund.currencyvaluesservice.repository.KeyStringValueRepository;
import com.jfund.currencyvaluesservice.service.KeyStringValueService;
import com.jfund.jfundclilib.UpdateOrCreateData;
import com.mongodb.MongoException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
@Slf4j(topic = "errors")
@Component
public class SimpleCurrencyKeysValueSaver implements CurrencyKeysValueSaver{
    private KeyStringValueService keyStringValueService;
    private final String dataKey = "currencyKeysValue";

    @Autowired
    public void setKeyStringValueService(KeyStringValueService keyStringValueService) {
        this.keyStringValueService = keyStringValueService;
    }
    @Override
    public UpdateOrCreateData save(String currencyKeysValue) {
        try{
            keyStringValueService.put(this.dataKey, currencyKeysValue);
            return new UpdateOrCreateData().setUpdateCount(1);
        }catch(MongoException e){
            log.error(e.getMessage());
            return new UpdateOrCreateData().setErrorMessage(e.getMessage());
        }
    }
}
