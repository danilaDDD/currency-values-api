package com.jfund.currencyvaluesservice.service;

import com.jfund.currencyvaluesservice.entity.CurrencyTimeStamp;
import com.jfund.currencyvaluesservice.entity.CurrencyValue;
import com.jfund.currencyvaluesservice.exceptions.LoadCurrencyKeysException;
import currencyapi.api.AsyncCurrencyApi;
import currencyapi.data.ApiCurrencyPair;
import currencyapi.data.ApiCurrencyValue;
import currencyapi.exceptions.CurrencyRequestException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
public class LoadCurrencyTimeStampService {
    private final AsyncCurrencyApi currencyApi;

    public Mono<CurrencyTimeStamp> loadCurrencyTimeStamp(Flux<String> keysFlux) {
        return keysFlux
                .collectList()
                .flatMap(this::getTimeStamps);
    }

    private Mono<CurrencyTimeStamp> getTimeStamps(List<String> keys) {
        if(keys.isEmpty()){
            return Mono.error(new LoadCurrencyKeysException("given currency keys is empty"));
        }

        try {
            Mono<List<CurrencyValue>> valueListMono = getValuesFromApi(keys)
                    .flatMap(values -> {
                        if (values.isEmpty()) {
                            return Mono.error(new LoadCurrencyKeysException("no currency values loaded"));
                        }
                        return Mono.just(values);
                    });

            return valueListMono
                    .map(currencyValues -> new CurrencyTimeStamp(LocalDateTime.now(), currencyValues));
        }catch(CurrencyRequestException e){
            return Mono.error(new LoadCurrencyKeysException(e));
        }
    }

    private Mono<List<CurrencyValue>> getValuesFromApi(List<String> keys) {

            CompletableFuture<List<ApiCurrencyValue>> future = currencyApi
                    .getCurrencyValues(keys.stream().map(ApiCurrencyPair::of).toList());

            return Mono.fromFuture(future)
                    .map(apiCurrencyValues -> apiCurrencyValues.stream()
                            .map(apiCurrencyValue -> new CurrencyValue(
                                    apiCurrencyValue.apiCurrencyPair().getKey(),
                                    apiCurrencyValue.value())).toList())
                    /**
                     * this exception throw in ApiCurrencyPair::of method if key is invalid
                     */
                    .onErrorMap(IllegalArgumentException.class, LoadCurrencyKeysException::new);

    }
}
