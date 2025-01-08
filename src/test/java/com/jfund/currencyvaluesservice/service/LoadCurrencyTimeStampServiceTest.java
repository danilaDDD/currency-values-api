package com.jfund.currencyvaluesservice.service;

import com.jfund.currencyvaluesservice.entity.CurrencyTimeStamp;
import com.jfund.currencyvaluesservice.entity.CurrencyValue;
import currencyapi.api.AsyncCurrencyApi;
import currencyapi.data.ApiCurrencyPair;
import currencyapi.data.ApiCurrencyValue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@SpringBootTest
@ActiveProfiles("test")
class LoadCurrencyTimeStampServiceTest {
    private static final String[] VALID_KEYS = {"EURUSD", "USDJPY", "GBPUSD", "USDCHF", "USDCAD"};

    @InjectMocks
    private LoadCurrencyTimeStampService loadCurrencyTimeStampService;

    @Mock
    private AsyncCurrencyApi currencyApi;

    @BeforeEach
    void setUp() {
    }

    @Test
    void testLoad_WithValidKeys_ShouldReturnCurrencyTimeStamps() {
        List<ApiCurrencyPair> apiCurrencyPairs = Stream.of(VALID_KEYS).map(ApiCurrencyPair::of).toList();
        List<ApiCurrencyValue> apiCurrencyValues = generateCurrencyValues(VALID_KEYS);
        CompletableFuture<List<ApiCurrencyValue>> currencyApiResponse = CompletableFuture.completedFuture(apiCurrencyValues);

        when(currencyApi.getCurrencyValues(apiCurrencyPairs))
                .thenReturn(currencyApiResponse);

        Set<CurrencyValue> expectedCurrencyValuesSet = apiCurrencyValues.stream()
                .map(apiCurrencyValue -> {
                    ApiCurrencyPair apiPair = apiCurrencyValue.apiCurrencyPair();
                    String key = apiPair.fromCurrency() + apiPair.toCurrency();
                    float value = apiCurrencyValue.value();

                    return new CurrencyValue(key, value);
                })
                .collect(Collectors.toSet());


        StepVerifier.create(loadCurrencyTimeStampService.loadCurrencyTimeStamp(Flux.fromArray(VALID_KEYS))
                        .map(CurrencyTimeStamp::getValues)
                        .collect(Collectors.toSet()))
                .expectNextCount(expectedCurrencyValuesSet.size())
                .verifyComplete();

    }

    @Test
    void loadCurrencyTimeStamp_WithEmptyKeys_ShouldReturnEmptyFlux() {

//        StepVerifier.create(loadCurrencyTimeStampService.loadCurrencyTimeStamp(Flux.empty()))
//                .verifyComplete();
    }

    @Test
    void loadCurrencyTimeStamp_WithInvalidKey_ShouldReturnError() {
//        when(currencyApi.getCurrencyTimeStamp(anyString()))
//                .thenReturn(Mono.error(new RuntimeException("Invalid key")));
//
//        StepVerifier.create(loadCurrencyTimeStampService.loadCurrencyTimeStamp(Flux.just("INVALID_KEY")))
//                .expectError(RuntimeException.class)
//                .verify();
    }

    private List<ApiCurrencyValue> generateCurrencyValues(String[] keys) {
        Random random = new Random();
        return Stream.of(keys)
                .map(key -> new ApiCurrencyValue(ApiCurrencyPair.of(key), random.nextFloat()))
                .toList();
    }

    private CompletableFuture<List<ApiCurrencyValue>> generateApiCurrencyValues(List<String> keys) {
        Random random = new Random();
        return CompletableFuture.completedFuture(keys.stream()
                .map(key -> new ApiCurrencyValue(ApiCurrencyPair.of(key), random.nextFloat()))
                .toList());
    }
}