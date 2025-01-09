package com.jfund.currencyvaluesservice.service;

import com.jfund.currencyvaluesservice.entity.CurrencyTimeStamp;
import com.jfund.currencyvaluesservice.entity.CurrencyValue;
import com.jfund.currencyvaluesservice.exceptions.SaveNewValuesException;
import com.jfund.currencyvaluesservice.repository.CurrencyTimeStampRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Set;

@SpringBootTest
@ActiveProfiles("test")
class SaveCurrencyTimeStampServiceTest {

    @Autowired
    private CurrencyTimeStampRepository repository;

    @Autowired
    private SaveCurrencyTimeStampService service;

    @BeforeEach
    void setUp() {
       repository.deleteAll().block();
    }

    @Test
    void testSave_WithInputValuesEmpty_ShouldThrowException() {
        CurrencyTimeStamp inputTimeStamp = new CurrencyTimeStamp(LocalDateTime.now(), Collections.emptySet());
        Mono<CurrencyTimeStamp> inputTimeStampMono = Mono.just(inputTimeStamp);

        StepVerifier.create(service.saveCurrencyTimeStampIfDifferent(inputTimeStampMono))
                .expectError(SaveNewValuesException.class)
                .verify();
    }

    @Test
    void testSave_WithNoDifferentValues_ShouldNotInsert() {
        CurrencyValue value = new CurrencyValue("USDEUR", 1.0f);
        CurrencyTimeStamp inputTimeStamp = new CurrencyTimeStamp(LocalDateTime.now(), Set.of(value));
        CurrencyTimeStamp lastTimeStamp = new CurrencyTimeStamp(LocalDateTime.now().minusDays(1), Set.of(value));

        lastTimeStamp = repository.save(lastTimeStamp).block();
        inputTimeStamp = service.saveCurrencyTimeStampIfDifferent(Mono.just(inputTimeStamp)).block();

        StepVerifier.create(repository.findAll())
                .expectNextSequence(Set.of(lastTimeStamp))
                .verifyComplete();
    }

    @Test
    void testSave_WithDifferentValues_ShouldInsertNewTimeStamp() {
        CurrencyValue value1 = new CurrencyValue("USDJPG", 1.0f);
        CurrencyValue value2 = new CurrencyValue("EURJPG", 0.9f);
        CurrencyTimeStamp inputTimeStamp = new CurrencyTimeStamp(LocalDateTime.now(), Set.of(value1, value2));
        CurrencyTimeStamp lastTimeStamp = new CurrencyTimeStamp(LocalDateTime.now().minusDays(1), Set.of(value1));
        lastTimeStamp = repository.save(lastTimeStamp).block();

        inputTimeStamp = service.saveCurrencyTimeStampIfDifferent(Mono.just(inputTimeStamp)).block();

        StepVerifier.create(repository.findFirstByOrderByDateTimeDesc())
                .expectNext(inputTimeStamp)
                .verifyComplete();
    }
}