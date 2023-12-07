package com.jfund.currencyvaluesservice.saver;

import com.jfund.currencyvaluesservice.entity.ChangeCurrencyValuesEvent;
import com.jfund.currencyvaluesservice.entity.CurrencyTimeStamp;
import com.jfund.currencyvaluesservice.entity.CurrencyValue;
import com.jfund.currencyvaluesservice.exceptions.ConsumingDataSourceException;
import com.jfund.currencyvaluesservice.exceptions.CurrencyRequestException;
import com.jfund.currencyvaluesservice.service.ChangeCurrencyValuesService;
import com.jfund.currencyvaluesservice.service.CurrencyTimeStampService;
import com.jfund.jfundclilib.UpdateOrCreateData;
import com.mongodb.MongoException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.*;

@Slf4j(topic = "errors")
@Component
public class ConcurrentNewCurrencyKeyValueSaver implements NewCurrencyValueSaver {
    private CurrencyTimeStampService timeStampService;

    private LocalDateTime actualDateTime;

    private final float accuracyForDifferentFloat = 0.0000001F;
    private ChangeCurrencyValuesService changeCurrencyValuesService;
    @Autowired
    public void setTimeStampService(CurrencyTimeStampService timeStampService) {
        this.timeStampService = timeStampService;
    }
    @Autowired
    public void setChangeCurrencyValuesService(ChangeCurrencyValuesService changeCurrencyValuesService){
        this.changeCurrencyValuesService = changeCurrencyValuesService;
    }

    @Override
    public UpdateOrCreateData save(Map<String, Float> currencyKeyValue) {
        try{
            return handle(currencyKeyValue);
        }catch (ConsumingDataSourceException | CurrencyRequestException | MongoException e){
            log.error(e.getMessage());
            return new UpdateOrCreateData().setErrorMessage(e.getMessage());
        }
    }
    private UpdateOrCreateData handle(Map<String, Float> inputCurrencyKeyValue){
        if(!inputCurrencyKeyValue.isEmpty()){
            this.actualDateTime = LocalDateTime.now();
            Optional<CurrencyTimeStamp> lastTimeStamp = timeStampService.findLastTimeStamp();
            List<CurrencyValue> inputCurrencyValues = inputCurrencyKeyValue.entrySet()
                    .stream().map(entry -> new CurrencyValue(entry.getKey(), entry.getValue())).toList();

            if(lastTimeStamp.isEmpty()){
                return createFirstTimeStamp(inputCurrencyValues);
            }else{
                CurrencyTimeStamp newTimeStamp = new CurrencyTimeStamp(this.actualDateTime, inputCurrencyValues);
                return createNewTimeStampIfDifferent(inputCurrencyKeyValue, lastTimeStamp.get(), newTimeStamp);
            }
        }

        return new UpdateOrCreateData();
    }
    private UpdateOrCreateData createNewTimeStampIfDifferent(Map<String, Float> inputCurrencyKeyValue, CurrencyTimeStamp lastTimeStamp, CurrencyTimeStamp newTimeStamp) {
        Map<String, Float> lastCurrencyKeyValue = new HashMap<>();
        lastTimeStamp.getValues().forEach(currencyValue -> lastCurrencyKeyValue.put(currencyValue.getKey(), currencyValue.getValue()));
        List<CurrencyValue> differentCurrencyValues = getDifferentCurrencyValues(inputCurrencyKeyValue, lastCurrencyKeyValue);

        if(!differentCurrencyValues.isEmpty()){
            timeStampService.saveEntity(newTimeStamp);

            ChangeCurrencyValuesEvent changeCurrencyValues = new ChangeCurrencyValuesEvent(differentCurrencyValues, this.actualDateTime);
            this.changeCurrencyValuesService.save(changeCurrencyValues);

            return new UpdateOrCreateData().setCreateCount(1);
        }else return new UpdateOrCreateData();
    }

    private List<CurrencyValue> getDifferentCurrencyValues(Map<String, Float> inputCurrencyKeyValue, Map<String, Float> lastCurrencyValues){
        Set<String> unionCurrencyKeysSet = new HashSet<>(lastCurrencyValues.keySet());
        unionCurrencyKeysSet.retainAll(inputCurrencyKeyValue.keySet());

        List<CurrencyValue> differentCurrencyValues = new ArrayList<>();

        for(String currencyKey: unionCurrencyKeysSet){
            float inputValue = inputCurrencyKeyValue.get(currencyKey);
            float lastValue = lastCurrencyValues.get(currencyKey);
            if(isDifferent(inputValue, lastValue)){
                differentCurrencyValues.add(new CurrencyValue(currencyKey, inputValue));
            }
        }

        return differentCurrencyValues;
    }

    private boolean isDifferent(float inputValue, float lastValue) {
        return Math.abs(inputValue - lastValue) >= accuracyForDifferentFloat;
    }
    private UpdateOrCreateData createFirstTimeStamp(List<CurrencyValue> inputCurrencyKeyValue) {
         timeStampService.saveEntity(new CurrencyTimeStamp(actualDateTime, inputCurrencyKeyValue));

         ChangeCurrencyValuesEvent changeCurrencyValues = new ChangeCurrencyValuesEvent(inputCurrencyKeyValue, this.actualDateTime);
         this.changeCurrencyValuesService.save(changeCurrencyValues);

         return new UpdateOrCreateData().setCreateCount(1);
    }
}
