package com.jfund.currencyvaluesservice.testutils;

import com.jfund.testutils.datagenerating.PrettyWordGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;
@Component
public class CustomerTestUtils {

    private DatabaseManager databaseManager;

    @Autowired
    public void setDatabaseManager(DatabaseManager databaseManager) {
        this.databaseManager = databaseManager;
    }

    public void dropCurrencyValuesTable(){
        databaseManager.dropCollection("timeStamps");
    }

    public Map<String, Float> generateRandomCurrencyKeyValue(){
        int wordLength = 6;
        int wordCount = 20;
        Random random = new Random();
        Map<String, Float> currencyValues = new HashMap<>();
        PrettyWordGenerator generator = new PrettyWordGenerator(wordLength);
        for (int i = 0; i < wordCount && generator.hasNext(); i++) {
            String key = generator.next().toLowerCase();
            float value = random.nextFloat();
            currencyValues.put(key, value);
        }

        return currencyValues;
    }
}
