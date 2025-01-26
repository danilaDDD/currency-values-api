package com.jfund.currencyvaluesservice.runner;

import com.jfund.currencyvaluesservice.exceptions.SendChangeValuesException;
import com.jfund.currencyvaluesservice.service.CurrencyTimeStampSerializerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

import java.util.concurrent.ExecutionException;

@Slf4j
@Component
@RequiredArgsConstructor
public class ChangeCurrencyValuesProducerRunner implements Runnable {
    @Value("${app.kafka.currency-values-topic}")
    private String topic;
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final CurrencyTimeStampSerializerService serializer;

    public void run() {
        serializer
                .getSerializedFlux()
                        .flatMap(stringEvent ->{
                            try {
                                return Flux.just(kafkaTemplate.send(topic, stringEvent).get());
                            } catch (InterruptedException | ExecutionException e) {
                                return Flux.error(new SendChangeValuesException(e));
                            }
                        })
                .doOnError(e -> log.error(e.getMessage()))
                .subscribe();
    }
}

