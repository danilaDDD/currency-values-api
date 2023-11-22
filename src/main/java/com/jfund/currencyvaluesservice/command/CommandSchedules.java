package com.jfund.currencyvaluesservice.command;

import com.jfund.jfundclilib.CliRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

@Configuration
@EnableScheduling
public class CommandSchedules {
    private CliRunner loadCurrencyValues;
    @Autowired
    public void setLoadCurrencyValues(LoadCurrencyValues loadCurrencyValues) {
        this.loadCurrencyValues = loadCurrencyValues;
    }
    public void loadCurrencyValues(){
        loadCurrencyValues.invoke();
    }
}
