package com.jfund.currencyvaluesservice.config;

import com.jfund.currencyvaluesservice.saver.CurrencyKeysValueSaver;
import com.jfund.currencyvaluesservice.saver.SimpleCurrencyKeysValueSaver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CustomerBeanConfig {
    @Bean
    public CurrencyKeysValueSaver currencyKeysValueSaver(){
        return new SimpleCurrencyKeysValueSaver();
    }
}
