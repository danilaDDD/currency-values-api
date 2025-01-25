package com.jfund.currencyvaluesservice.service;

import com.jfund.currencyvaluesservice.entity.CurrencyTimeStamp;
import com.jfund.currencyvaluesservice.entity.CurrencyValue;
import com.jfund.currencyvaluesservice.repository.ChangeCurrencyValuesEventRepository;
import com.jfund.currencyvaluesservice.repository.CurrencyTimeStampRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import reactor.test.StepVerifier;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.util.AssertionErrors.assertEquals;

@SpringBootTest
@ActiveProfiles("test")
class CurrencyTimeStampCRUDServiceSaveTest {
    private static final String[] CURRENCY_KEYS = {"EURUSD", "USDJPY", "GBPUSD", "USDCHF", "USDCAD", "AUDUSD", "NZDUSD", "EURGBP", "EURJPY", "GBPJPY"};
    private static final Random RANDOM = new Random();

    @Autowired
    private CurrencyTimeStampCRUDService service;
    @Autowired
    private ChangeCurrencyValuesEventRepository changeCurrencyValuesEventRepository;
    @Autowired
    private CurrencyTimeStampRepository currencyTimeStampRepository;

    @BeforeEach
    public void setUp(){
        changeCurrencyValuesEventRepository.deleteAll().block();
        currencyTimeStampRepository.deleteAll().block();
    }

    @Test
    void testSaveCurrencyTimeStamp_ShouldSaveChangeCurrencyValuesEvent() {
        CurrencyTimeStamp currencyTimeStamp = generateTimeStamp(LocalDateTime.now());

        CurrencyTimeStamp savedTimeStamp = service.save(currencyTimeStamp).block();

        Assertions.assertNotNull(savedTimeStamp);

        StepVerifier.create(changeCurrencyValuesEventRepository.findAll())
                .assertNext(actualEvent -> {
                    assertEquals("equals values test", new HashSet<>(savedTimeStamp.getValues()), new HashSet<>(actualEvent.getChangedValues()));
                    assertEquals("equals timestamp test", truncated(savedTimeStamp.getDateTime()), truncated(actualEvent.getChangedDateTime()));
                    assertTrue(actualEvent.getActual());
                })
                .verifyComplete();
    }

    private LocalDateTime truncated(LocalDateTime dateTime) {
        return dateTime.truncatedTo(ChronoUnit.MILLIS);
    }

    @Test
    void testSave10CurrencyTimeStamp_ShouldSave10ChangeCurrencyValuesEvent() {
        int count = 10;
        List<CurrencyTimeStamp> currencyTimeStamps = Stream.generate(() -> generateTimeStamp(LocalDateTime.now()))
                .limit(10)
                .toList();

        currencyTimeStamps.forEach(timeStamp -> service.save(timeStamp).block());

        StepVerifier.create(changeCurrencyValuesEventRepository.findAll())
                .expectNextCount(count)
                .verifyComplete();

        CurrencyTimeStamp lastTimeStamp = currencyTimeStampRepository.findFirstByOrderByDateTimeDesc().block();
        StepVerifier.create(changeCurrencyValuesEventRepository.findFirstByOrderByChangedDateTimeDesc())
                .assertNext(actualEvent -> {
                    assertEquals("equals values test for last event", new HashSet<>(lastTimeStamp.getValues()), new HashSet<>(actualEvent.getChangedValues()));
                    assertEquals("equals timestamp test for last event", truncated(lastTimeStamp.getDateTime()), truncated(actualEvent.getChangedDateTime()));
                    assertTrue(actualEvent.getActual());
                })
                .verifyComplete();

    }

    private CurrencyTimeStamp generateTimeStamp(LocalDateTime localDateTime) {
        List<CurrencyValue> currencyValues = Stream.of(CURRENCY_KEYS)
                .map(key -> new CurrencyValue(key, RANDOM.nextFloat()))
                .toList();

        return new CurrencyTimeStamp(localDateTime, currencyValues);
    }


}