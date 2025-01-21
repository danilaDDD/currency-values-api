package com.jfund.currencyvaluesservice.command;

import com.jfund.currencyvaluesservice.runner.ChangeCurrencyValuesProducerRunner;
import com.jfund.currencyvaluesservice.runner.LoadCurrencyTimeStampRunner;
import lombok.RequiredArgsConstructor;

import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

@Configuration
@EnableScheduling
@RequiredArgsConstructor
public class CommandSchedules {
    private final LoadCurrencyTimeStampRunner loadCurrencyTimeStampRunner;
    private final ChangeCurrencyValuesProducerRunner changeCurrencyValuesSender;

    @Scheduled(fixedRate = 500000)
    public void loadCurrencyValues(){
        loadCurrencyTimeStampRunner.run();
    }

    @Scheduled(fixedRate = 30000)
    public void sendCurrencyValuesChanges(){
        changeCurrencyValuesSender.run();
    }


}
