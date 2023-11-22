package com.jfund.currencyvaluesservice.config;

import com.jfund.currencyvaluesservice.request.GetCurrencyValuesRequest;
import com.jfund.currencyvaluesservice.request.SimpleGetCurrencyValuesRequest;
import com.jfund.currencyvaluesservice.saver.ConcurrentNewCurrencyKeyValueSaver;
import com.jfund.currencyvaluesservice.saver.CurrencyKeysValueSaver;
import com.jfund.currencyvaluesservice.saver.NewCurrencyValueSaver;
import com.jfund.currencyvaluesservice.saver.SimpleCurrencyKeysValueSaver;
import com.jfund.jfundclilib.UpdateOrCreateData;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CustomerBeanConfig {
    @Bean
    public CurrencyKeysValueSaver currencyKeysValueSaver(){
        return new SimpleCurrencyKeysValueSaver();
    }

    @Bean
    public NewCurrencyValueSaver newCurrencyValueSaver(){
        return new ConcurrentNewCurrencyKeyValueSaver();
    }

    @Bean
    public GetCurrencyValuesRequest getCurrencyValuesRequest(){
        return new SimpleGetCurrencyValuesRequest();
    }
}
