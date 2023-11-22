package com.jfund.currencyvaluesservice.command;

import com.jfund.jfundclilib.CliRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

@Configuration
@EnableScheduling
public class CommandSchedules {
    private CliRunner loadCurrencyValues;
    @Autowired
    public void setLoadCurrencyValues(LoadCurrencyValues loadCurrencyValues) {
        this.loadCurrencyValues = loadCurrencyValues;
    }
//    @Scheduled(fixedRate = 50000)
    public void loadCurrencyValues(){
        loadCurrencyValues.invoke();
    }
}
