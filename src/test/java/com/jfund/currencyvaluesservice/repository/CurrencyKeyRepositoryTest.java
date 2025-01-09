package com.jfund.currencyvaluesservice.repository;

import com.jfund.currencyvaluesservice.entity.CurrencyKey;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import reactor.test.StepVerifier;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
class CurrencyKeyRepositoryTest {
    @Autowired
    private CurrencyKeyRepository currencyKeyRepository;

    @BeforeEach
    public void setUp() {
        currencyKeyRepository.deleteAll().block();
    }

    @Test
    void testSave_WithValidCurrencyKey_ShouldSaveCurrencyKey() {
        CurrencyKey currencyKey = new CurrencyKey("EURUSD");
        CurrencyKey savedCurrencyKey = currencyKeyRepository.save(currencyKey).block();

        StepVerifier.create(currencyKeyRepository.findAll())
                .expectNext(savedCurrencyKey)
                .verifyComplete();
    }
}