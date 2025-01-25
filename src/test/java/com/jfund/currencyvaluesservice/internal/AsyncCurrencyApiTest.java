package com.jfund.currencyvaluesservice.internal;

import com.jfund.currencyvaluesservice.entity.CurrencyValue;
import currencyapi.api.AsyncCurrencyApi;
import currencyapi.data.ApiCurrencyPair;
import currencyapi.data.ApiCurrencyValue;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.concurrent.ExecutionException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

@SpringBootTest
@ActiveProfiles("test")
public class AsyncCurrencyApiTest {
    @Autowired
    private AsyncCurrencyApi asyncCurrencyApi;

    @Test
    public void testGetCurrencyValuesShouldSuccessfully() throws ExecutionException, InterruptedException {
        List<ApiCurrencyPair> keysList = asyncCurrencyApi.getCurrencyPairs()
                .get().stream()
                .toList();

        List<ApiCurrencyValue> values = asyncCurrencyApi
                .getCurrencyValues(keysList).get().stream()
                .toList();

        assertNotEquals(0, values.size());
    }
}
