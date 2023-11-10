package com.jfund.currencyvaluesservice.saver;

import com.jfund.jfundclilib.UpdateOrCreateData;

public interface CurrencyKeysValueSaver {
    UpdateOrCreateData save(String currencyKeysValue);
}
