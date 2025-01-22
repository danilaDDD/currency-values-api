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
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
@ActiveProfiles("test")
class SaveCurrencyTimeStampServiceTest {
    @Autowired
    private CurrencyTimeStampRepository repository;

    @Autowired
    private SaveCurrencyTimeStampService service;

    @BeforeEach
    public void setUp(){
        repository.deleteAll().block();
    }

    @Test
    void testSave_WhenNotLastTimeStamp_ThenInsertInputTimeStamp() {
        CurrencyTimeStamp inputTimeStamp = generateTimeStamp();
        CurrencyTimeStamp savedTimeStamp = service
                .saveCurrencyTimeStampIfDifferent(Mono.just(inputTimeStamp))
                .block();

        assertEquals(inputTimeStamp, savedTimeStamp);

        StepVerifier.create(repository.findAll())
                .expectNext(savedTimeStamp)
                .verifyComplete();

        StepVerifier.create(repository.count())
                .expectNext(1L)
                .verifyComplete();
    }

    @Test
    void testSave_WhenLastTimeStampWithAllDifferentValues_ThenInsertTimeStampWithAllValues(){
        CurrencyTimeStamp lastTimeStamp = generateTimeStamp();
        repository.save(lastTimeStamp).block();

        List<CurrencyValue> differentValues = lastTimeStamp.getValues().stream()
                .map(value -> new CurrencyValue(value.getKey(), value.getValue() + 1))
                .toList();
        CurrencyTimeStamp inputTimeStamp = new CurrencyTimeStamp(LocalDateTime.now(), differentValues);

        CurrencyTimeStamp savedInputTimeStamp = service
                .saveCurrencyTimeStampIfDifferent(Mono.just(inputTimeStamp))
                .block();

        assertEquals(inputTimeStamp, savedInputTimeStamp);
        verifyTimeStampInDatabase(savedInputTimeStamp, lastTimeStamp);
    }

    @Test
    void testSave_WhenLastTimeStampWithSomeDifferentValues_ThenInsertTimeStampWithDifferentValues(){
        CurrencyTimeStamp lastTimeStamp = generateTimeStamp();
        repository.save(lastTimeStamp).block();

        List<CurrencyValue> differentValues = Stream.of("EURUSD", "EURJPY", "EURGBP", "EURRUB")
                .map(currency -> new CurrencyValue(currency, new Random().nextFloat()))
                .toList();

        CurrencyTimeStamp inputTimeStamp = new CurrencyTimeStamp(LocalDateTime.now(), differentValues);

        CurrencyTimeStamp savedInputTimeStamp = service
                .saveCurrencyTimeStampIfDifferent(Mono.just(inputTimeStamp))
                .block();

        assertEquals(inputTimeStamp, savedInputTimeStamp);
        assertEquals(new HashSet<>(differentValues),
                    new HashSet<>(savedInputTimeStamp.getValues()));
        verifyTimeStampInDatabase(savedInputTimeStamp, lastTimeStamp);
    }

    @Test
    void testSave_WhenLastTimeStampWithNotDifferentValues_ThenNotInsert(){
        CurrencyTimeStamp lastTimeStamp = generateTimeStamp();
        repository.save(lastTimeStamp).block();

        CurrencyTimeStamp inputTimeStamp = new CurrencyTimeStamp(LocalDateTime.now(), lastTimeStamp.getValues());

        CurrencyTimeStamp savedInputTimeStamp = service
                .saveCurrencyTimeStampIfDifferent(Mono.just(inputTimeStamp))
                .block();

        assertNull(savedInputTimeStamp);

        StepVerifier.create(repository.findAll())
                .expectNext(lastTimeStamp)
                .verifyComplete();

        StepVerifier.create(repository.count())
                .expectNextCount(1L)
                .verifyComplete();
    }

    @Test
    void testSaveTimeStampWithEmptyValues_ShouldThrowSaveNewValuesException(){
        CurrencyTimeStamp lastTimeStamp = generateTimeStamp();
        repository.save(lastTimeStamp).block();

        CurrencyTimeStamp inputTimeStamp = new CurrencyTimeStamp(LocalDateTime.now(), List.of());

        StepVerifier.create(service.saveCurrencyTimeStampIfDifferent(Mono.just(inputTimeStamp)))
                .expectError(SaveNewValuesException.class)
                .verify();

    }

    @Test
    void testSaveTimeStampWithDateTimeLessThanExist_ShouldThrowSaveNewValuesException(){
        CurrencyTimeStamp lastTimeStamp = generateTimeStamp();
        repository.save(lastTimeStamp).block();

        CurrencyTimeStamp inputTimeStamp = new CurrencyTimeStamp(lastTimeStamp.getDateTime().minusSeconds(1),
                generateRandomValues());

        StepVerifier.create(service.saveCurrencyTimeStampIfDifferent(Mono.just(inputTimeStamp)))
                .expectError(SaveNewValuesException.class)
                .verify();
    }

    private void verifyTimeStampInDatabase(CurrencyTimeStamp inputTimeStamp, CurrencyTimeStamp lastTimeStamp) {
        StepVerifier.create(repository.findByOrderByDateTimeDesc())
                .expectNext(inputTimeStamp)
                .expectNext(lastTimeStamp)
                .verifyComplete();

        StepVerifier.create(repository.count())
                .expectNext(2L)
                .verifyComplete();
    }

    private CurrencyTimeStamp generateTimeStamp(){
        return new CurrencyTimeStamp(
                LocalDateTime.now(),
                generateRandomValues()
        );
    }

    private List<CurrencyValue> generateRandomValues() {
        Random random = new Random();

        return Stream.of("EURUSD", "EURJPY", "EURGBP", "EURCHF", "EURCAD", "EURAUD", "EURHKD", "EURSGD", "EURSEK", "EURNOK")
                .map(currency -> new CurrencyValue(currency, random.nextFloat()))
                .toList();
    }
}