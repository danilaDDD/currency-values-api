package com.jfund.currencyvaluesservice.runner;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jfund.currencyvaluesservice.exceptions.SendChangeValuesException;
import com.jfund.currencyvaluesservice.service.ChangeCurrencyValuesEventService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

import java.util.concurrent.ExecutionException;

@Component
@RequiredArgsConstructor
public class ChangeCurrencyValuesProducerRunner implements Runnable {
    @Value("${app.kafka.currency-values-topic}")
    private String topic;
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ChangeCurrencyValuesEventService eventService;
    private final ObjectMapper objectMapper;

    public void run() {
        Flux<String> stringEventFlux = eventService.getSerializedFlux();

        stringEventFlux.toStream()
                .forEach(event -> {
                    try {
                        kafkaTemplate.send(topic, event).get();
                    } catch (InterruptedException | ExecutionException e) {
                        throw new SendChangeValuesException(e);
                    }
                });
    }
}

