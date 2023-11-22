package com.jfund.currencyvaluesservice.saver;

import com.jfund.jfundclilib.UpdateOrCreateData;
import org.springframework.stereotype.Component;

import java.util.Map;
public interface NewCurrencyValueSaver {
    UpdateOrCreateData save(Map<String, Float> currencyKeyValue);
}
