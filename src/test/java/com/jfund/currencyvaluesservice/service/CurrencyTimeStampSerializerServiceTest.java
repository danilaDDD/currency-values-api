package com.jfund.currencyvaluesservice.service;

import com.jfund.currencyvaluesservice.entity.CurrencyTimeStamp;
import com.jfund.currencyvaluesservice.entity.CurrencyValue;
import com.jfund.currencyvaluesservice.exceptions.SendChangeValuesException;
import com.jfund.currencyvaluesservice.repository.CurrencyTimeStampRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
class CurrencyTimeStampSerializerServiceTest {
    private static final Map<String, Float> KEY_VALUE_MAP = Map.of("EURUSD", 1.677f, "EURAUD", 0.8666f,
            "EURRUB", 75.0f, "EURJPY", 125.0f, "EURGBP", 0.9f, "EURCHF", 1.1f, "EURCAD", 1.3f, "EURCNY", 7.0f);
    @Autowired
    private CurrencyTimeStampSerializerService service;
    @Autowired
    private CurrencyTimeStampRepository repository;

    @BeforeEach
    public void setUp(){
        repository.deleteAll().block();
    }

    @Test
    void testSerialize_WhenExistActualTimeStamp_ThenReturnSerializeThis() {
        List<CurrencyValue> values = KEY_VALUE_MAP.entrySet().stream()
                .map(entry -> new CurrencyValue(entry.getKey(), entry.getValue()))
                .toList();
        LocalDateTime dateTime = LocalDateTime.now();
        repository
                .save(new CurrencyTimeStamp(dateTime, values, false))
                .block();

        StepVerifier.create(service.getSerializedFlux())
                .expectNextCount(1)
                .verifyComplete();
    }

    @Test
    void testSerialize_WhenNotExistTimeStamp_ThenReturnEmptyFlux(){
        StepVerifier.create(service.getSerializedFlux())
                .expectNextCount(0)
                .verifyComplete();

    }

    @Test
    void testSerialize_WhenExist2ActualTimeStamp_ThenReturnSerializeThis(){
        LocalDateTime dateTime = LocalDateTime.now();
        List<CurrencyValue> values1 = KEY_VALUE_MAP.entrySet().stream()
                .map(entry -> new CurrencyValue(entry.getKey(), entry.getValue()))
                .toList();
        repository
                .save(new CurrencyTimeStamp(dateTime, values1, false))
                .block();

        List<CurrencyValue> values2 = values1.stream()
                .map(value1 -> new CurrencyValue(value1.getKey(), value1.getValue() + 1))
                .toList();
        repository
                .save(new CurrencyTimeStamp(dateTime.plusMinutes(1), values2, false))
                .block();

        StepVerifier.create(service.getSerializedFlux())
                .expectNextCount(2)
                .verifyComplete();
    }

    @Test
    void testSerialize_WhenExist2NotActualTimeStamp_ThenReturnEmptyFlux(){
        LocalDateTime dateTime = LocalDateTime.now();
        List<CurrencyValue> values1 = KEY_VALUE_MAP.entrySet().stream()
                .map(entry -> new CurrencyValue(entry.getKey(), entry.getValue()))
                .toList();
        repository
                .save(new CurrencyTimeStamp(dateTime, values1, true))
                .block();

        List<CurrencyValue> values2 = values1.stream()
                .map(value1 -> new CurrencyValue(value1.getKey(), value1.getValue() + 1))
                .toList();
        repository
                .save(new CurrencyTimeStamp(dateTime.plusMinutes(1), values2, true))
                .block();

        StepVerifier.create(service.getSerializedFlux())
                .expectNextCount(0)
                .verifyComplete();
    }

    @Test
    void testSerialize_WhenExistTimeStampButEmptyValues_ThenThrowSendChangeValuesException(){
        repository
                .save(new CurrencyTimeStamp(LocalDateTime.now(), List.of(), false))
                .block();

        StepVerifier.create(service.getSerializedFlux())
                .expectError(SendChangeValuesException.class)
                .verify();

    }

}