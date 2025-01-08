package com.jfund.currencyvaluesservice.service;

import com.jfund.currencyvaluesservice.repository.CurrencyTimeStampRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CurrencyTimeStampCRUDService {
    private final CurrencyTimeStampRepository currencyTimeStampRepository;
}
