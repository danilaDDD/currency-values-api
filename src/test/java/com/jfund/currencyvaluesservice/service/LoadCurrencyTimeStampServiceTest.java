package com.jfund.currencyvaluesservice.service;

import com.jfund.currencyvaluesservice.entity.CurrencyTimeStamp;
import com.jfund.currencyvaluesservice.entity.CurrencyValue;
import com.jfund.currencyvaluesservice.exceptions.LoadCurrencyKeysException;
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
import static org.mockito.Mockito.when;

@SpringBootTest
@ActiveProfiles("test")
class LoadCurrencyTimeStampServiceTest {
    private static final String[] VALID_KEYS = {"EURUSD", "USDJPY", "GBPUSD", "USDCHF", "USDCAD"};

    @InjectMocks
    private LoadCurrencyTimeStampService loadCurrencyTimeStampService;

    @Mock
    private AsyncCurrencyApi currencyApi;


    // @FIXME: verify by count is bad practice
    @Test
    void testLoad_WithValidKeys_ShouldReturnCurrencyTimeStamps() {
        List<ApiCurrencyPair> apiCurrencyPairs = generateApiCurrencyPairs(VALID_KEYS);
        List<ApiCurrencyValue> apiCurrencyValues = generateCurrencyValues(VALID_KEYS);
        CompletableFuture<List<ApiCurrencyValue>> currencyApiResponse = CompletableFuture.completedFuture(apiCurrencyValues);

        when(currencyApi.getCurrencyValues(apiCurrencyPairs))
                .thenReturn(currencyApiResponse);

        int expectedCount = apiCurrencyValues.size();

        Flux<CurrencyValue> actualValuesFlux = loadCurrencyTimeStampService.loadCurrencyTimeStamp(Flux.fromArray(VALID_KEYS))
                .map(CurrencyTimeStamp::getValues)
                .flatMapMany(Flux::fromIterable);


        StepVerifier.create(actualValuesFlux)
                .expectNextCount(expectedCount)
                .verifyComplete();

    }

    @Test
    void testLoad_WithEmptyResponse_ShouldReturnLoadCurrencyKeysException() {
        List<ApiCurrencyPair> apiPairs = generateApiCurrencyPairs(VALID_KEYS);
        when(currencyApi.getCurrencyValues(apiPairs))
                .thenReturn(CompletableFuture.completedFuture(List.of()));

        StepVerifier.create(loadCurrencyTimeStampService.loadCurrencyTimeStamp(Flux.fromArray(VALID_KEYS)))
                .expectError(LoadCurrencyKeysException.class)
                .verify();
    }

    private List<ApiCurrencyValue> generateCurrencyValues(String[] keys) {
        Random random = new Random();
        return Stream.of(keys)
                .map(key -> new ApiCurrencyValue(ApiCurrencyPair.of(key), random.nextFloat()))
                .toList();
    }

    private List<ApiCurrencyPair> generateApiCurrencyPairs(String[] keys) {
        return Stream.of(keys)
                .map(ApiCurrencyPair::of)
                .toList();
    }
}