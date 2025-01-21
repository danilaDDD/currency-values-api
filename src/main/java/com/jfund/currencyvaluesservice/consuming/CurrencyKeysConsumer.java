package com.jfund.currencyvaluesservice.consuming;

import com.jfund.currencyvaluesservice.service.ConsumingCurrencyKeysService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class CurrencyKeysConsumer {
    private static final Logger logger = LoggerFactory.getLogger(CurrencyKeysConsumer.class);
    private final ConsumingCurrencyKeysService consumerService;

    @KafkaListener(topics = "currency-keys-topic", groupId = "currency-keys-group")
    public void listenAvailableCurrencyKeys(String currencyKeysValue){
        consumerService.upgradeCurrencyKeys(currencyKeysValue).subscribe();
    }
}
