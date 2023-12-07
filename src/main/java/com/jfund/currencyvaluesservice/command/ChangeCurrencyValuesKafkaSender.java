package com.jfund.currencyvaluesservice.command;

import com.jfund.currencyvaluesservice.entity.ChangeCurrencyValuesEvent;
import com.jfund.currencyvaluesservice.producer.ChangeCurrencyValuesProducer;
import com.jfund.currencyvaluesservice.service.ChangeCurrencyValuesService;
import com.jfund.jfundclilib.CliRunner;
import com.jfund.jfundclilib.UpdateOrCreateData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j(topic = "errors")
@Component
public class ChangeCurrencyValuesKafkaSender implements CliRunner {
    private ChangeCurrencyValuesProducer changeCurrencyValuesProducer;
    private ChangeCurrencyValuesService changeCurrencyValuesService;

    @Autowired
    public void setChangeCurrencyValuesProducer(ChangeCurrencyValuesProducer changeCurrencyValuesProducer) {
        this.changeCurrencyValuesProducer = changeCurrencyValuesProducer;
    }

    @Autowired
    public void setChangeCurrencyValuesService(ChangeCurrencyValuesService changeCurrencyValuesService) {
        this.changeCurrencyValuesService = changeCurrencyValuesService;
    }

    public UpdateOrCreateData invoke(){
        System.out.println("start send currency values");
        List<ChangeCurrencyValuesEvent> changes = this.changeCurrencyValuesService.findAll();
        AtomicInteger deleteCount = new AtomicInteger();
        List<String> errors = new ArrayList<>();

        changes.forEach(changeCurrencyValues -> {
            try{
                this.changeCurrencyValuesProducer.send(changeCurrencyValues);
                this.changeCurrencyValuesService.delete(changeCurrencyValues);
                deleteCount.getAndIncrement();
            }catch (ChangeCurrencyValuesProducer.SendException e){
                log.error(e.getMessage());
                errors.add(e.getMessage());
            }
        });

        var result = new UpdateOrCreateData()
                .setDeleteCount(deleteCount.get());
        if(!errors.isEmpty())
            result = result.setErrorMessage(String.join("/n", errors));

        return result;
    }
}
