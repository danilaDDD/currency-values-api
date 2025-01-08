package com.jfund.currencyvaluesservice.consuming;

import com.jfund.currencyvaluesservice.service.ConsumingCurrencyKeysService;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CurrencyKeysConsumer {
    private final ConsumingCurrencyKeysService consumerService;

    @KafkaListener(topics = "currency-keys-topic", groupId = "currency-keys-group")
    public void listenAvailableCurrencyKeys(String currencyKeysValue){
        consumerService.upgradeCurrencyKeys(currencyKeysValue).subscribe();
    }
}
