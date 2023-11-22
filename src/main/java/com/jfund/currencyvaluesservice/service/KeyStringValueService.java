package com.jfund.currencyvaluesservice.service;

import com.jfund.currencyvaluesservice.entity.KeyStringValueEntity;
import com.jfund.currencyvaluesservice.exceptions.ConsumingDataValueNotFoundException;
import com.jfund.currencyvaluesservice.repository.KeyStringValueRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class KeyStringValueService {
    private KeyStringValueRepository keyStringValueRepository;

    @Autowired
    public void setKeyStringValueRepository(KeyStringValueRepository keyStringValueRepository) {
        this.keyStringValueRepository = keyStringValueRepository;
    }

    public KeyStringValueEntity put(String key, String stringValue){
        KeyStringValueEntity entity = new KeyStringValueEntity(key, stringValue);
        entity.onUpdate();
        return keyStringValueRepository.save(entity);
    }

    public String getStringValue(String key) throws ConsumingDataValueNotFoundException {
         Optional<KeyStringValueEntity> entityOptional = keyStringValueRepository.findById(key);
         if(entityOptional.isPresent()){
             return entityOptional.get().getStringValue();
         }else{
             throw new ConsumingDataValueNotFoundException("entity by key %s not found");
         }
    }
}
