package com.jfund.currencyvaluesservice.command;

import com.jfund.currencyvaluesservice.consuming.ConsumerDataSource;
import com.jfund.currencyvaluesservice.request.GetCurrencyValuesRequest;
import com.jfund.currencyvaluesservice.request.SimpleCurrencyRequestArgs;
import com.jfund.currencyvaluesservice.saver.NewCurrencyValueSaver;
import com.jfund.jfundclilib.CliRunner;
import com.jfund.jfundclilib.UpdateOrCreateData;
import com.mongodb.MongoException;
import currencyapi.exceptions.CurrencyBadRequestException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public class LoadCurrencyValues implements CliRunner {
    private GetCurrencyValuesRequest getCurrencyValuesRequest;
    private NewCurrencyValueSaver newCurrencyValueSaver;
    private ConsumerDataSource consumerDataSource;

    @Autowired
    public void setGetCurrencyValuesRequest(GetCurrencyValuesRequest getCurrencyValuesRequest) {
        this.getCurrencyValuesRequest = getCurrencyValuesRequest;
    }

    @Qualifier("newCurrencyValueSaver")
    @Autowired
    public void setNewCurrencyValueSaver(NewCurrencyValueSaver newCurrencyValueSaver) {
        this.newCurrencyValueSaver = newCurrencyValueSaver;
    }

    @Autowired
    public void setConsumerDataSource(ConsumerDataSource consumerDataSource) {
        this.consumerDataSource = consumerDataSource;
    }

    @Override
    public UpdateOrCreateData invoke() {
        return handle();
    }

    private UpdateOrCreateData handle(){
        List<String> currencyKeys = consumerDataSource.getCurrencyKeys();
        Map<String, Float> currencyValuesFromApi = getCurrencyValuesRequest.getResponse(new SimpleCurrencyRequestArgs().setCurrencyKeys(currencyKeys));
        return newCurrencyValueSaver.save(currencyValuesFromApi);
    }
}
