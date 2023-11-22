package com.jfund.currencyvaluesservice.testutils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;
@Component
public class CustomerTestUtils {

    private DatabaseManager databaseManager;
    private CurrencyDataGenerator currencyDataGenerator;

    @Autowired
    public void setDatabaseManager(DatabaseManager databaseManager) {
        this.databaseManager = databaseManager;
    }

    @Autowired
    public void setCurrencyDataGenerator(CurrencyDataGenerator currencyDataGenerator) {
        this.currencyDataGenerator = currencyDataGenerator;
    }

    public Map<String, Float> generateCurrencyKeys(int size){
        return currencyDataGenerator.generate(size);
    }

    public void dropCurrencyValuesTable(){
        databaseManager.dropCollection("timeStamps");
    }
}
