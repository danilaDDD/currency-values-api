package com.jfund.currencyvaluesservice.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import currencyapi.api.AsyncCurrencyApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CustomConfig {
    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }

    @Bean
    public AsyncCurrencyApi getAsyncCurrencyApi() {
        return AsyncCurrencyApi.open();
    }
}
