package com.jfund.currencyvaluesservice.service;

import com.jfund.currencyvaluesservice.entity.ChangeCurrencyValuesEvent;
import com.jfund.currencyvaluesservice.repository.ChangedCurrencyValuesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ChangeCurrencyValuesService {
    private ChangedCurrencyValuesRepository changedCurrencyValuesRepository;
    @Autowired
    public void setChangedCurrencyValuesRepository(ChangedCurrencyValuesRepository changedCurrencyValuesRepository){
        this.changedCurrencyValuesRepository = changedCurrencyValuesRepository;
    }
    public List<ChangeCurrencyValuesEvent> findAll(){
        return changedCurrencyValuesRepository.findAll();
    }

    public void delete(ChangeCurrencyValuesEvent changeCurrencyValues){
        changedCurrencyValuesRepository.delete(changeCurrencyValues);
    }

    public ChangeCurrencyValuesEvent save(ChangeCurrencyValuesEvent changeCurrencyValues){
        return changedCurrencyValuesRepository.save(changeCurrencyValues);
    }

}
