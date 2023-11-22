package com.jfund.currencyvaluesservice.request;

import com.jfund.currencyvaluesservice.exceptions.CurrencyRequestException;
import currencyapi.exceptions.CurrencyBadRequestException;
import currencyapi.handler.GetAllCurrencyValuesRequestHandler;
import org.springframework.beans.factory.annotation.Value;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public class SimpleGetCurrencyValuesRequest implements GetCurrencyValuesRequest{
    @Value("${app.currencyApi.accessKey")
    private String accessKey;
    @Override
    public Map<String, Float> getResponse(CurrencyRequestArgs args) {
        try {
            SimpleCurrencyRequestArgs simpleCurrencyRequestArgs = (SimpleCurrencyRequestArgs) args;
            List<String> currencyKeys = simpleCurrencyRequestArgs.getCurrencyKeys();
            return new GetAllCurrencyValuesRequestHandler(accessKey, currencyKeys).handle();

        } catch (CurrencyBadRequestException e) {
            throw new CurrencyRequestException(e);
        }catch (ExecutionException e){
            throw new CurrencyRequestException(e);
        }catch (InterruptedException e){
            throw new CurrencyRequestException(e);
        }
    }
}
