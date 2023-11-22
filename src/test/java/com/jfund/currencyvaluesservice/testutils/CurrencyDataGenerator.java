package com.jfund.currencyvaluesservice.testutils;

import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.*;
@Component
public class CurrencyDataGenerator {
    public Map<String, Float> generate(int size){
        Set<String> currencyKeys = generateRandomKeys(size);
        Map<String, Float> result = new HashMap<>();
        Random random = new Random();
        currencyKeys.forEach(key -> result.put(key, random.nextFloat()));

        return Map.of("USDRUB", random.nextFloat(), "JPGRUB", random.nextFloat(), "USDJPG", random.nextFloat());
    }

    private Set<String> generateRandomKeys(int size){
        Set<String> result = new HashSet<>();
        byte[] array = new byte[6];
        for(int i = 0; i < size; i++) {
            new Random().nextBytes(array);
            result.add(new String(array, StandardCharsets.UTF_8));
        }

        return result;
    }
}
