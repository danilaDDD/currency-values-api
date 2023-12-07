package com.jfund.currencyvaluesservice.producer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jfund.currencyvaluesservice.entity.ChangeCurrencyValuesEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class ChangeCurrencyValuesProducer {
    @Value("${app.kafka.currency-values-topic}")
    private String topic;
    private KafkaTemplate<String, String> kafkaTemplate;
    private ObjectMapper objectMapper;

    @Autowired
    public void setKafkaTemplate(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @Autowired
    public void setObjectMapper(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public void send(ChangeCurrencyValuesEvent changeCurrencyValuesEvent) {
        try {
            String stringEvent = objectMapper.writeValueAsString(changeCurrencyValuesEvent);
            kafkaTemplate.send(topic, changeCurrencyValuesEvent.getId(), stringEvent).get();
        } catch (Exception e) {
            throw new SendException(e);
        }
    }

    public static class SendException extends RuntimeException{
        public SendException(Exception e){
            super(e);
        }
    }
}
