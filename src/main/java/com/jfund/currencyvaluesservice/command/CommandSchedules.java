package com.jfund.currencyvaluesservice.command;

import com.jfund.jfundclilib.CliRunner;
import com.jfund.jfundclilib.UpdateOrCreateData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

@Configuration
@EnableScheduling
public class CommandSchedules {
    private CliRunner loadCurrencyValues;
    private CliRunner changeCurrencyValuesKafkaSender;

    @Autowired
    public void setLoadCurrencyValues(LoadCurrencyValues loadCurrencyValues) {
        this.loadCurrencyValues = loadCurrencyValues;
    }
    @Autowired
    public void setChangeCurrencyValuesKafkaSender(ChangeCurrencyValuesKafkaSender changeCurrencyValuesKafkaSender) {
        this.changeCurrencyValuesKafkaSender = changeCurrencyValuesKafkaSender;
    }
    @Scheduled(fixedRate = 500000)
    public void loadCurrencyValues(){
        UpdateOrCreateData invokeData = loadCurrencyValues.invoke();
        System.out.println("end load currency values from api");
        System.out.println(invokeData);
    }

    @Scheduled(fixedRate = 30000)
    public void sendCurrencyValuesChanges(){
        UpdateOrCreateData result = this.changeCurrencyValuesKafkaSender.invoke();
        System.out.println(result.toString());
    }


}
