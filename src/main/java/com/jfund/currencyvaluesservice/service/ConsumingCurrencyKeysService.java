package com.jfund.currencyvaluesservice.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jfund.currencyvaluesservice.data.ConsumingCurrencyKeysObject;
import com.jfund.currencyvaluesservice.entity.CurrencyKey;
import com.jfund.currencyvaluesservice.exceptions.ConsumingDataSourceException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ConsumingCurrencyKeysService {
    private final ObjectMapper objectMapper;
    private final CurrencyKeyService currencyKeyService;

    /**
     * input object example:
     * {"serializedData":"[\"EURUSD\",\"EURJPG\",\"USDJPG\",\"USDEUR\",\"EURAUD\",\"USDAUD\",\"AURGBP\"]",
     * "empty":false,
     * "createdAt":"2025-01-09T18:36:15.890916053"}
     */

    public Mono<Void> upgradeCurrencyKeys(String serializedKeysValueObject) {
        Flux<CurrencyKey> currencyKeysFlux = deserialize(serializedKeysValueObject);
        return currencyKeyService.updateCurrencyKeys(currencyKeysFlux);
    }

    private Flux<CurrencyKey> deserialize(String string) {
        try {
            ConsumingCurrencyKeysObject keysObject = objectMapper
                    .readValue(string, ConsumingCurrencyKeysObject.class);

            List<CurrencyKey> currencyKeyList = objectMapper
                    .readValue(keysObject.getSerializedData(), new TypeReference<List<String>>(){})
                    .stream().map(CurrencyKey::new).toList();
            return Flux.fromIterable(currencyKeyList);
        } catch (JsonProcessingException e) {
            return Flux.error(new ConsumingDataSourceException(String.format("parsing object %s with exception", string)));

        }
    }
}
