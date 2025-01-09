package com.jfund.currencyvaluesservice.service;

import com.jfund.currencyvaluesservice.entity.ChangeCurrencyValuesEvent;
import com.jfund.currencyvaluesservice.entity.CurrencyKey;
import com.jfund.currencyvaluesservice.exceptions.ConsumingDataSourceException;
import com.jfund.currencyvaluesservice.repository.ChangeCurrencyValuesEventRepository;
import com.jfund.currencyvaluesservice.repository.CurrencyKeyRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.ActiveProfiles;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.util.Comparator;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
class ConsumingCurrencyKeysServiceTest {
    @Autowired
    private ConsumingCurrencyKeysService consumingService;
    @Autowired
    private CurrencyKeyRepository currencyKeyRepository;

    @BeforeEach
    void setUp() {
        currencyKeyRepository.deleteAll().block();
    }

    @Test
    void testUpgrade_WithValidData_ShouldInsertCurrencyKeys() {
        String string = """
                {"serializedData":"[\\"EURUSD\\",\\"EURJPG\\"]",
                 "empty":false,
                  "createdAt":"2025-01-09T18:36:15.890916053"}
                """;
        consumingService.upgradeCurrencyKeys(string).block();

        Flux<String> actual = currencyKeyRepository.findAll().map(CurrencyKey::getKey).sort(String::compareTo);
        StepVerifier
                .create(actual)
                .expectNextSequence(List.of("EURJPG", "EURUSD"))
                .verifyComplete();
    }

    @Test
    void testUpgrade_WithInvalidData_ShouldThrowConsumingDataSourceException(){
        String string = """
                {"serial":"[\\"EURUSD\\",\\"EURJPG\\"]",
                 "empty":false,
                  "createdAt":"2025-01-09T18:36:15.890916053"}
                """;

        StepVerifier.create(consumingService.upgradeCurrencyKeys(string))
                .expectError(ConsumingDataSourceException.class)
                .verify();


    }
}